package com.jiuth.bigevent.validation;

import com.jiuth.bigevent.anno.State;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

//ContraintValidator<<拾哪个注解提供校验规则,校验的数据类型>
public class StateValidation implements ConstraintValidator<State,String> {

    /***
     * 提供校验规则
     * @param s 要检验的数据
     * @param constraintValidatorContext
     * @return false 不通过，true 通过
     */
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        //提供校验规则
//提供校验规则
        if (s == null) {
            return false;
        }
        if(s.equals("已发布")|| s.equals("草稿") ){
            return true;
        }
        return false;
    }
}
