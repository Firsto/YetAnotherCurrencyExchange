package studio.inprogress.yace.app.ui.main;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import studio.inprogress.yace.app.R;
import studio.inprogress.yace.app.databinding.ActivityMainBinding;
import studio.inprogress.yace.app.di.CreateComponentException;
import studio.inprogress.yace.app.di.component.UserComponent;
import studio.inprogress.yace.app.model.api.response.CurrencyResponse;
import studio.inprogress.yace.app.ui.adapter.item.CurrencyItem;
import studio.inprogress.yace.app.ui.base.BaseActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity
        implements MainMvpView, SwipeRefreshLayout.OnRefreshListener, FastAdapter.OnClickListener<CurrencyItem> {

    @InjectPresenter
    MainPresenter presenter;

    UserComponent userComponent;

    private ActivityMainBinding binding;
    private FastItemAdapter<CurrencyItem> currenciesAdapter;

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

        if (currenciesAdapter == null) {
            currenciesAdapter = new FastItemAdapter<>();
        }
        currenciesAdapter.withOnClickListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.currenciesList.setLayoutManager(layoutManager);
        binding.currenciesList.setAdapter(currenciesAdapter);
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
        currenciesAdapter.clear();
        CurrencyItem baseItem = new CurrencyItem(new CurrencyResponse(currencyResponse.getBase(), currencyResponse.getBase(), currencyResponse.getRates(), new ObservableField<>(1.0), 1.0));
        baseItem.setWatcher(watcher);
        currenciesAdapter.add(baseItem);

        HashMap<String, Double> rates = currencyResponse.getRates();
        for (Map.Entry<String, Double> rate : rates.entrySet()) {
            if (rate.getKey().equals(baseItem.getCurrencies().getBase())) continue;
            CurrencyResponse response = new CurrencyResponse(rate.getKey(), "", new HashMap<>(), new ObservableField<>(), rate.getValue());
            CurrencyItem currencyItem = new CurrencyItem(response);
            currencyItem.getCurrencies().getAmount().set(rate.getValue());
            currenciesAdapter.add(currencyItem);
        }
    }

    @Override
    public void clearData() {
        currenciesAdapter.clear();
    }

    @Override
    public boolean onClick(View v, IAdapter<CurrencyItem> adapter, CurrencyItem item, int position) {
        showToast("Click " + item.getCurrencies().getBase());
        return false;
    }

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (TextUtils.isEmpty(s)) return;
            List<CurrencyItem> adapterItems = currenciesAdapter.getAdapterItems();
            for (CurrencyItem item : adapterItems) {
                item.getCurrencies().getAmount().set(Double.parseDouble(s.toString()) * item.getCurrencies().getRate());
            }
        }
    };
}
