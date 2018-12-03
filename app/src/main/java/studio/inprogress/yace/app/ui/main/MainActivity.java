package studio.inprogress.yace.app.ui.main;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;
import com.mikepenz.fastadapter.listeners.TouchEventHook;
import studio.inprogress.yace.app.R;
import studio.inprogress.yace.app.databinding.ActivityMainBinding;
import studio.inprogress.yace.app.di.CreateComponentException;
import studio.inprogress.yace.app.di.component.UserComponent;
import studio.inprogress.yace.app.model.api.response.CurrencyResponse;
import studio.inprogress.yace.app.ui.adapter.item.CurrencyItem;
import studio.inprogress.yace.app.ui.base.BaseActivity;
import timber.log.Timber;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends BaseActivity
        implements MainMvpView, SwipeRefreshLayout.OnRefreshListener, OnClickListener {

    @InjectPresenter
    MainPresenter presenter;

    private UserComponent userComponent;

    private ActivityMainBinding binding;
    private FastAdapter itemFastAdapter;
    private ItemAdapter<CurrencyItem> baseCurrencyAdapter;
    private ItemAdapter<CurrencyItem> currenciesAdapter;

    private Observable.OnPropertyChangedCallback onBaseItemChangeAmountCallback = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            updateCurrencies(baseCurrencyAdapter.getAdapterItem(0).getCurrencies());
        }
    };

    @ProvidePresenter
    public MainPresenter providePresenter() {
        return userComponent.provideMainPresenter();
    }

    @Override
    protected void initComponents() throws CreateComponentException {
        userComponent = getComponentManager().createUserComponent();
    }

    @Override
    protected void releaseComponents() {
        getComponentManager().releaseUserComponent();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initUI();
    }

    private void initUI() {
        binding.refresher.setOnRefreshListener(this);

        if (itemFastAdapter == null) {
            itemFastAdapter = new FastAdapter();
        }

        if (baseCurrencyAdapter == null) {
            baseCurrencyAdapter = new ItemAdapter<>();
        }
        if (currenciesAdapter == null) {
            currenciesAdapter = new ItemAdapter<>();
        }

        itemFastAdapter = FastAdapter.with(Arrays.asList(baseCurrencyAdapter, currenciesAdapter));
        itemFastAdapter.withOnClickListener(this);

        itemFastAdapter.withEventHook(new TouchEventHook() {
            @Override
            public View onBind(RecyclerView.ViewHolder viewHolder) {
                return viewHolder.itemView.findViewById(R.id.amount);
            }

            @Override
            public boolean onTouch(View v, MotionEvent event, int position, FastAdapter fastAdapter, IItem item) {
                if (position > 0) fastAdapter.getOnClickListener().onClick(v, currenciesAdapter, item, position);
                return false;
            }
        });

        binding.currenciesList.setAdapter(itemFastAdapter);
    }

    @Override
    public void onRefresh() {
        presenter.refresh();
    }

    private void cancelRefreshing() {
        binding.refresher.post(() -> binding.refresher.setRefreshing(false));
    }

    @Override
    public void showProgress() {
        binding.refresher.setRefreshing(true);
    }

    @Override
    public void showError(Throwable e) {
        cancelRefreshing();

        // TODO: implement error handler
        showToast(e.getMessage());
    }

    @Override
    public void showCurrencies(CurrencyResponse currencyResponse) {
        cancelRefreshing();
        if (currenciesAdapter.getAdapterItemCount() == 0) {
            initAdapterData(currencyResponse);
        } else {
            baseCurrencyAdapter.getAdapterItem(0).getCurrencies().getRates().putAll(currencyResponse.getRates());
            itemFastAdapter.notifyAdapterDataSetChanged();

            updateCurrencies(currencyResponse);
        }
//        binding.currenciesList.postDelayed(this::hideKeyboard, 100);
    }

    private void initAdapterData(CurrencyResponse currencyResponse) {
        baseCurrencyAdapter.clear();
        currenciesAdapter.clear();
        CurrencyItem baseItem = new CurrencyItem(new CurrencyResponse(currencyResponse.getBase(), currencyResponse.getDate(), currencyResponse.getRates(), 1.0));
        baseItem.withTag(currencyResponse);
        baseCurrencyAdapter.add(baseItem);

        baseItem.getCurrencies().getAmount().addOnPropertyChangedCallback(onBaseItemChangeAmountCallback);

        Map<String, Double> rates = currencyResponse.getRates();
        for (Map.Entry<String, Double> rate : rates.entrySet()) {
            if (rate.getKey().equals(baseItem.getCurrencies().getBase())) continue;
            CurrencyResponse response = new CurrencyResponse(rate.getKey(), currencyResponse.getDate(), new HashMap<>(), rate.getValue());
            CurrencyItem currencyItem = new CurrencyItem(response);
            currencyItem.withTag(response);
            currencyItem.getCurrencies().getAmount().set(rate.getValue());
            currenciesAdapter.add(currencyItem);
        }
    }

    private void updateCurrencies(CurrencyResponse currencyResponse) {
        //TODO: to be refactored...
        if (currencyResponse.getRates().isEmpty()) return;

        for (CurrencyItem item : currenciesAdapter.getAdapterItems()) {
//            Timber.d(" item: " + item.getCurrencies().getBase());
            try {
                item.getCurrencies().getAmount().set(baseCurrencyAdapter.getAdapterItem(0).getCurrencies().getAmount().get() * currencyResponse.getRates().get(item.getCurrencies().getBase()).doubleValue());
            } catch (Exception e) {
                e.printStackTrace();

                Timber.d("error at item " + item.getCurrencies().getBase());
            }
        }
    }

    @Override
    public void clearData() {
        baseCurrencyAdapter.clear();
        currenciesAdapter.clear();
    }

    @Override
    public boolean onClick(View v, IAdapter adapter, IItem item, int position) {
        if (position == 0) return false;
        if (item instanceof CurrencyItem) {
            presenter.loadCurrencies(((CurrencyItem) item).getCurrencies().getBase());

            CurrencyItem baseItem = baseCurrencyAdapter.getAdapterItem(0);
            baseItem.getCurrencies().getAmount().removeOnPropertyChangedCallback(onBaseItemChangeAmountCallback);

            currenciesAdapter.remove(position);
            currenciesAdapter.add(position, baseItem);

            baseCurrencyAdapter.clear();
            baseCurrencyAdapter.add(((CurrencyItem) item));

            ((CurrencyItem) item).getCurrencies().getAmount().addOnPropertyChangedCallback(onBaseItemChangeAmountCallback);

            itemFastAdapter.notifyAdapterDataSetChanged();

            Objects.requireNonNull(binding.currenciesList.getLayoutManager()).scrollToPosition(0);
        }
        return false;
    }
}
