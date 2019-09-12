package com.benben.megamart.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.benben.megamart.R;


/**
 * 自定义增加购买数量的控件
 */
public class NumberView extends RelativeLayout {
    private boolean editable = true;
    private Button  btnNumEdit;
    private RelativeLayout btnReduce, btnAdd;

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public NumberView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        LayoutInflater.from(context).inflate(R.layout.layout_number_view, this, true);
        btnReduce = (RelativeLayout) findViewById(R.id.rlyt_cart_reduce);
        btnAdd = (RelativeLayout) findViewById(R.id.rlyt_cart_add);
        btnNumEdit = (Button) findViewById(R.id.btn_cart_num_edit);

        if (btnNumEdit.getText().toString().equals("0")){
            btnReduce.setEnabled(false);
        }

        btnAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editable) {
                    btnReduce.setEnabled(true);
                    int num = getNum(btnNumEdit);
                    num++;

                    btnNumEdit.setText("" + num);

                    if (onNumberChagneListener != null) {
                        onNumberChagneListener.onChange(num);
                    }
                }
            }
        });
        btnReduce.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editable) {
                    int num = getNum(btnNumEdit);
                    num--;

                    btnNumEdit.setText("" + num);

                    if (num < 1) {
                        btnReduce.setEnabled(false);
                    }

                    if (onNumberChagneListener != null) {
                        onNumberChagneListener.onChange(num);
                    }
                }
            }
        });
    }

    /**
     * 获取数字
     *
     * @param tvNum
     * @return
     */
    private int getNum(TextView tvNum) {
        String num = tvNum.getText().toString().trim();
        return Integer.valueOf(num);

    }

    /**
     * 获取数字
     *
     *
     * @return
     */
    private int getNum() {
        String num = btnNumEdit.getText().toString().trim();
        return Integer.valueOf(num);
    }

    private Context context;
    private int num = 1;
    private OnNumberChangeListener onNumberChagneListener = null;

    public interface OnNumberChangeListener {
        public void onChange(int newnum);
    }

    public void setOnNumberChangeListener(OnNumberChangeListener templistener) {
        if (templistener != null) {
            this.onNumberChagneListener = templistener;
        }
    }

    public void setNum(int num) {
        this.num = num;
        refreshNum();
    }

    //	public int getNum(){
//		return this.num ;
//	}
    private void refreshNum() {
        btnNumEdit.setText(num + "");
        if (num < 1) {
            btnReduce.setEnabled(false);
        }else{
            btnReduce.setEnabled(true);
        }


    }

    public int getnum() {
        String a = btnNumEdit.getText().toString().trim();
        return Integer.valueOf(a);
    }

    public void setUnEnable() {
        btnAdd.setEnabled(false);
        btnReduce.setEnabled(false);
    }


}
