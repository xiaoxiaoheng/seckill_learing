package com.miaomiao.exception;

import com.miaomiao.result.CodeMsg;

/**
 * Created by cjq on 2018-03-07 19:19
 */
public class GlobalException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    private CodeMsg cm;

    public GlobalException(CodeMsg cm) {
        super(cm.toString());
        this.cm = cm;
    }

    public CodeMsg getCm() {
        return cm;
    }
}
