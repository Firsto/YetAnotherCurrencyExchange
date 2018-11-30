package studio.inprogress.yace.app.ui.adapter.item;

import android.support.v7.widget.RecyclerView;
import android.text.TextWatcher;
import android.view.View;
import com.mikepenz.fastadapter.items.AbstractItem;
import studio.inprogress.yace.app.R;
import studio.inprogress.yace.app.databinding.ItemCurrencyBinding;
import studio.inprogress.yace.app.model.api.response.CurrencyResponse;

import java.util.List;

public class CurrencyItem extends AbstractItem<CurrencyItem, CurrencyItem.ViewHolder> {

    private CurrencyResponse currencies;
    private TextWatcher watcher;

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
        if (watcher != null) {
            holder.binding.amount.addTextChangedListener(watcher);
        }
    }

    @Override
    public void unbindView(ViewHolder holder) {
        super.unbindView(holder);
        watcher = null;
    }

    public void setWatcher(TextWatcher watcher) {
        this.watcher = watcher;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ItemCurrencyBinding binding;

        public ItemCurrencyBinding getBinding() {
            return binding;
        }

        public ViewHolder(View itemView) {
            super(itemView);
            binding = ItemCurrencyBinding.bind(itemView);
        }
    }
}
