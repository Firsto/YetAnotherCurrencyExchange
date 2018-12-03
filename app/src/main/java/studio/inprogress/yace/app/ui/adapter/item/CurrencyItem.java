package studio.inprogress.yace.app.ui.adapter.item;

import android.view.View;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;
import studio.inprogress.yace.app.R;
import studio.inprogress.yace.app.databinding.ItemCurrencyBinding;
import studio.inprogress.yace.app.model.api.response.CurrencyResponse;
import studio.inprogress.yace.app.utils.TextBindingAdapter;

import java.util.List;

public class CurrencyItem extends AbstractItem<CurrencyItem, CurrencyItem.ViewHolder> {

    private CurrencyResponse currencies;

    public CurrencyItem(CurrencyResponse currencies) {
        super();
        this.currencies = currencies;
    }

    public CurrencyResponse getCurrencies() {
        return currencies;
    }

    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.currency_item;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_currency;
    }

    @Override
    public void bindView(ViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);
        holder.binding.setCurr(currencies);
    }

    protected static class ViewHolder extends FastAdapter.ViewHolder<CurrencyItem> {

        ItemCurrencyBinding binding;

        public ItemCurrencyBinding getBinding() {
            return binding;
        }

        public ViewHolder(View itemView) {
            super(itemView);
            binding = ItemCurrencyBinding.bind(itemView);
            binding.setConverter(new TextBindingAdapter());
        }

        @Override
        public void bindView(CurrencyItem item, List<Object> payloads) {

        }

        @Override
        public void unbindView(CurrencyItem item) {

        }
    }
}
