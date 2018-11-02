package com.fanwang.sg.weight;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fanwang.sg.R;

/**
 * 作者：yc on 2018/7/11.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class AddNumberView extends ConstraintLayout implements View.OnClickListener{

    private EditText etNumber;

    private int mTotalStock;

    public AddNumberView(Context context) {
        super(context);
        init(context);
    }

    public AddNumberView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AddNumberView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        LayoutInflater.from(context).inflate(R.layout.include_add, this);
        findViewById(R.id.rf_left).setOnClickListener(this);
        findViewById(R.id.rf_right).setOnClickListener(this);
        etNumber = findViewById(R.id.et_number);

        etNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(final Editable editable) {
                new Handler().postDelayed(new Runnable(){
                    public void run() {
                        if (editable.length() > 0){
                            int s = Integer.valueOf(editable.toString());
                            if (s > mTotalStock){
                                etNumber.setText(mTotalStock + "");
                                ToastUtils.showShort("数量超过当前库存");
                                return;
                            }
                        }
                    }
                }, 200);
            }
        });
        /*findViewById(R.id.view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null){
                    listener.onClick();
                }
            }
        });*/
    }

    public void setNumber(int number){
        etNumber.setText(number + "");
    }

    public int getNumber(){
        String number = etNumber.getText().toString().trim();
        if (StringUtils.isEmpty(number)){
            return 0;
        }else {
            return Integer.valueOf(number);
        }
    }

    private Integer integer = 0;

    @Override
    public void onClick(View view) {
        String number = etNumber.getText().toString().trim();
        if (!StringUtils.isEmpty(number)){
             integer = Integer.valueOf(number);
        }
        switch (view.getId()){
            case R.id.rf_left:
                if (integer > 1){
                    etNumber.setText((--integer) + "");
                }
                break;
            case R.id.rf_right:
                etNumber.setText((++integer) + "");
                break;
        }
    }


    public void setTotalStock(int totalStock) {
        mTotalStock = totalStock;
    }

    public int getTotalStock() {
        return mTotalStock;
    }

    private OnClickListener listener;
    public void setOnClickListener(OnClickListener listener){
        this.listener = listener;
    }
    public interface OnClickListener{
        void onClick();
    }

}
