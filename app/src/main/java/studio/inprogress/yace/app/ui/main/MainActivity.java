package studio.inprogress.yace.app.ui.main;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import studio.inprogress.yace.app.R;
import studio.inprogress.yace.app.databinding.ActivityMainBinding;
import studio.inprogress.yace.app.di.CreateComponentException;
import studio.inprogress.yace.app.di.component.UserComponent;
import studio.inprogress.yace.app.model.api.response.CurrencyResponse;
import studio.inprogress.yace.app.ui.adapter.item.CurrencyItem;
import studio.inprogress.yace.app.ui.base.BaseActivity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseActivity
        implements MainMvpView, SwipeRefreshLayout.OnRefreshListener {

    @InjectPresenter
    MainPresenter presenter;

    private UserComponent userComponent;

    private ActivityMainBinding binding;
    private FastAdapter itemFastAdapter;
    private ItemAdapter<CurrencyItem> baseCurrencyAdapter;
    private ItemAdapter<CurrencyItem> currenciesAdapter;

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
        initAdapterData(currencyResponse);
        binding.currenciesList.postDelayed(this::hideKeyboard, 100);
    }

    private void initAdapterData(CurrencyResponse currencyResponse) {
        baseCurrencyAdapter.clear();
        currenciesAdapter.clear();
        CurrencyItem baseItem = new CurrencyItem(new CurrencyResponse(currencyResponse.getBase(), currencyResponse.getDate(), currencyResponse.getRates(), new ObservableField<>(1.0), 1.0));
        baseItem.withTag(currencyResponse);
        baseItem.setWatcher(watcher);
        baseCurrencyAdapter.add(baseItem);

        HashMap<String, Double> rates = currencyResponse.getRates();
        for (Map.Entry<String, Double> rate : rates.entrySet()) {
            if (rate.getKey().equals(baseItem.getCurrencies().getBase())) continue;
            CurrencyResponse response = new CurrencyResponse(rate.getKey(), currencyResponse.getDate(), new HashMap<>(), new ObservableField<>(), rate.getValue());
            CurrencyItem currencyItem = new CurrencyItem(response);
            currencyItem.withTag(response);
            currencyItem.getCurrencies().getAmount().set(rate.getValue());
            currenciesAdapter.add(currencyItem);
        }
    }

    @Override
    public void clearData() {
        baseCurrencyAdapter.clear();
        currenciesAdapter.clear();
    }

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (TextUtils.isEmpty(s.toString())) return;
            for (CurrencyItem item : currenciesAdapter.getAdapterItems()) {
                item.getCurrencies().getAmount().set(Double.parseDouble(s.toString()) * item.getCurrencies().getRate());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

}
