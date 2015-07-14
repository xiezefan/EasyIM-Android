package me.xiezefan.easyim.mvp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.InjectView;
import butterknife.OnClick;
import me.xiezefan.easyim.R;
import rx.android.widget.WidgetObservable;

/**
 * Created by XieZeFan on 2015/4/21 0021.
 */
public class SearchActivity extends BaseActivity {


    // Toolbar
    @InjectView(R.id.etSearchText)
    EditText etSearchText;
    @InjectView(R.id.tvSearchText)
    TextView tvSearchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
    }

    private void initView() {
        WidgetObservable.text(etSearchText).subscribe(event -> {
            String text = event.text().toString();
            tvSearchText.setText(text);
            if (text.length() > 0 && text.charAt(text.length() - 1) == '\n') {
                text = text.substring(0, text.length() - 1);
                etSearchText.setText(text);
                etSearchText.setSelection(text.length());
                doSearch(text);
            }
        });
    }

    private void doSearch(String text) {
        if (TextUtils.isEmpty(text)) {
            //TODO show error dialog
            Toast.makeText(this, "搜索内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, SearchResultActivity.class);
        intent.putExtra(SearchResultActivity.KEY_SEARCH_TEXT, text);
        startActivity(intent);
    }



    @OnClick(R.id.ivCancel)
    public void clearSearchText(View view) {
        etSearchText.setText("");
    }

    @OnClick(R.id.llSearchBtnWrapper)
    public void doSearch(View view) {
        doSearch(etSearchText.getText().toString());
    }


}
