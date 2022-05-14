package com.teradata.portal.web.apiRelease.pojo;

import java.util.ArrayList;
import java.util.List;

public class calcPrincCodeExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public calcPrincCodeExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andCalcPrincIdIsNull() {
            addCriterion("calc_princ_id is null");
            return (Criteria) this;
        }

        public Criteria andCalcPrincIdIsNotNull() {
            addCriterion("calc_princ_id is not null");
            return (Criteria) this;
        }

        public Criteria andCalcPrincIdEqualTo(Integer value) {
            addCriterion("calc_princ_id =", value, "calcPrincId");
            return (Criteria) this;
        }

        public Criteria andCalcPrincIdNotEqualTo(Integer value) {
            addCriterion("calc_princ_id <>", value, "calcPrincId");
            return (Criteria) this;
        }

        public Criteria andCalcPrincIdGreaterThan(Integer value) {
            addCriterion("calc_princ_id >", value, "calcPrincId");
            return (Criteria) this;
        }

        public Criteria andCalcPrincIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("calc_princ_id >=", value, "calcPrincId");
            return (Criteria) this;
        }

        public Criteria andCalcPrincIdLessThan(Integer value) {
            addCriterion("calc_princ_id <", value, "calcPrincId");
            return (Criteria) this;
        }

        public Criteria andCalcPrincIdLessThanOrEqualTo(Integer value) {
            addCriterion("calc_princ_id <=", value, "calcPrincId");
            return (Criteria) this;
        }

        public Criteria andCalcPrincIdIn(List<Integer> values) {
            addCriterion("calc_princ_id in", values, "calcPrincId");
            return (Criteria) this;
        }

        public Criteria andCalcPrincIdNotIn(List<Integer> values) {
            addCriterion("calc_princ_id not in", values, "calcPrincId");
            return (Criteria) this;
        }

        public Criteria andCalcPrincIdBetween(Integer value1, Integer value2) {
            addCriterion("calc_princ_id between", value1, value2, "calcPrincId");
            return (Criteria) this;
        }

        public Criteria andCalcPrincIdNotBetween(Integer value1, Integer value2) {
            addCriterion("calc_princ_id not between", value1, value2, "calcPrincId");
            return (Criteria) this;
        }

        public Criteria andCalcPrincDescIsNull() {
            addCriterion("calc_princ_desc is null");
            return (Criteria) this;
        }

        public Criteria andCalcPrincDescIsNotNull() {
            addCriterion("calc_princ_desc is not null");
            return (Criteria) this;
        }

        public Criteria andCalcPrincDescEqualTo(String value) {
            addCriterion("calc_princ_desc =", value, "calcPrincDesc");
            return (Criteria) this;
        }

        public Criteria andCalcPrincDescNotEqualTo(String value) {
            addCriterion("calc_princ_desc <>", value, "calcPrincDesc");
            return (Criteria) this;
        }

        public Criteria andCalcPrincDescGreaterThan(String value) {
            addCriterion("calc_princ_desc >", value, "calcPrincDesc");
            return (Criteria) this;
        }

        public Criteria andCalcPrincDescGreaterThanOrEqualTo(String value) {
            addCriterion("calc_princ_desc >=", value, "calcPrincDesc");
            return (Criteria) this;
        }

        public Criteria andCalcPrincDescLessThan(String value) {
            addCriterion("calc_princ_desc <", value, "calcPrincDesc");
            return (Criteria) this;
        }

        public Criteria andCalcPrincDescLessThanOrEqualTo(String value) {
            addCriterion("calc_princ_desc <=", value, "calcPrincDesc");
            return (Criteria) this;
        }

        public Criteria andCalcPrincDescLike(String value) {
            addCriterion("calc_princ_desc like", value, "calcPrincDesc");
            return (Criteria) this;
        }

        public Criteria andCalcPrincDescNotLike(String value) {
            addCriterion("calc_princ_desc not like", value, "calcPrincDesc");
            return (Criteria) this;
        }

        public Criteria andCalcPrincDescIn(List<String> values) {
            addCriterion("calc_princ_desc in", values, "calcPrincDesc");
            return (Criteria) this;
        }

        public Criteria andCalcPrincDescNotIn(List<String> values) {
            addCriterion("calc_princ_desc not in", values, "calcPrincDesc");
            return (Criteria) this;
        }

        public Criteria andCalcPrincDescBetween(String value1, String value2) {
            addCriterion("calc_princ_desc between", value1, value2, "calcPrincDesc");
            return (Criteria) this;
        }

        public Criteria andCalcPrincDescNotBetween(String value1, String value2) {
            addCriterion("calc_princ_desc not between", value1, value2, "calcPrincDesc");
            return (Criteria) this;
        }

        public Criteria andCalcFormulaIsNull() {
            addCriterion("calc_formula is null");
            return (Criteria) this;
        }

        public Criteria andCalcFormulaIsNotNull() {
            addCriterion("calc_formula is not null");
            return (Criteria) this;
        }

        public Criteria andCalcFormulaEqualTo(String value) {
            addCriterion("calc_formula =", value, "calcFormula");
            return (Criteria) this;
        }

        public Criteria andCalcFormulaNotEqualTo(String value) {
            addCriterion("calc_formula <>", value, "calcFormula");
            return (Criteria) this;
        }

        public Criteria andCalcFormulaGreaterThan(String value) {
            addCriterion("calc_formula >", value, "calcFormula");
            return (Criteria) this;
        }

        public Criteria andCalcFormulaGreaterThanOrEqualTo(String value) {
            addCriterion("calc_formula >=", value, "calcFormula");
            return (Criteria) this;
        }

        public Criteria andCalcFormulaLessThan(String value) {
            addCriterion("calc_formula <", value, "calcFormula");
            return (Criteria) this;
        }

        public Criteria andCalcFormulaLessThanOrEqualTo(String value) {
            addCriterion("calc_formula <=", value, "calcFormula");
            return (Criteria) this;
        }

        public Criteria andCalcFormulaLike(String value) {
            addCriterion("calc_formula like", value, "calcFormula");
            return (Criteria) this;
        }

        public Criteria andCalcFormulaNotLike(String value) {
            addCriterion("calc_formula not like", value, "calcFormula");
            return (Criteria) this;
        }

        public Criteria andCalcFormulaIn(List<String> values) {
            addCriterion("calc_formula in", values, "calcFormula");
            return (Criteria) this;
        }

        public Criteria andCalcFormulaNotIn(List<String> values) {
            addCriterion("calc_formula not in", values, "calcFormula");
            return (Criteria) this;
        }

        public Criteria andCalcFormulaBetween(String value1, String value2) {
            addCriterion("calc_formula between", value1, value2, "calcFormula");
            return (Criteria) this;
        }

        public Criteria andCalcFormulaNotBetween(String value1, String value2) {
            addCriterion("calc_formula not between", value1, value2, "calcFormula");
            return (Criteria) this;
        }

        public Criteria andSubhdTypeIsNull() {
            addCriterion("subhd_type is null");
            return (Criteria) this;
        }

        public Criteria andSubhdTypeIsNotNull() {
            addCriterion("subhd_type is not null");
            return (Criteria) this;
        }

        public Criteria andSubhdTypeEqualTo(String value) {
            addCriterion("subhd_type =", value, "subhdType");
            return (Criteria) this;
        }

        public Criteria andSubhdTypeNotEqualTo(String value) {
            addCriterion("subhd_type <>", value, "subhdType");
            return (Criteria) this;
        }

        public Criteria andSubhdTypeGreaterThan(String value) {
            addCriterion("subhd_type >", value, "subhdType");
            return (Criteria) this;
        }

        public Criteria andSubhdTypeGreaterThanOrEqualTo(String value) {
            addCriterion("subhd_type >=", value, "subhdType");
            return (Criteria) this;
        }

        public Criteria andSubhdTypeLessThan(String value) {
            addCriterion("subhd_type <", value, "subhdType");
            return (Criteria) this;
        }

        public Criteria andSubhdTypeLessThanOrEqualTo(String value) {
            addCriterion("subhd_type <=", value, "subhdType");
            return (Criteria) this;
        }

        public Criteria andSubhdTypeLike(String value) {
            addCriterion("subhd_type like", value, "subhdType");
            return (Criteria) this;
        }

        public Criteria andSubhdTypeNotLike(String value) {
            addCriterion("subhd_type not like", value, "subhdType");
            return (Criteria) this;
        }

        public Criteria andSubhdTypeIn(List<String> values) {
            addCriterion("subhd_type in", values, "subhdType");
            return (Criteria) this;
        }

        public Criteria andSubhdTypeNotIn(List<String> values) {
            addCriterion("subhd_type not in", values, "subhdType");
            return (Criteria) this;
        }

        public Criteria andSubhdTypeBetween(String value1, String value2) {
            addCriterion("subhd_type between", value1, value2, "subhdType");
            return (Criteria) this;
        }

        public Criteria andSubhdTypeNotBetween(String value1, String value2) {
            addCriterion("subhd_type not between", value1, value2, "subhdType");
            return (Criteria) this;
        }

        public Criteria andArgFormIsNull() {
            addCriterion("arg_form is null");
            return (Criteria) this;
        }

        public Criteria andArgFormIsNotNull() {
            addCriterion("arg_form is not null");
            return (Criteria) this;
        }

        public Criteria andArgFormEqualTo(String value) {
            addCriterion("arg_form =", value, "argForm");
            return (Criteria) this;
        }

        public Criteria andArgFormNotEqualTo(String value) {
            addCriterion("arg_form <>", value, "argForm");
            return (Criteria) this;
        }

        public Criteria andArgFormGreaterThan(String value) {
            addCriterion("arg_form >", value, "argForm");
            return (Criteria) this;
        }

        public Criteria andArgFormGreaterThanOrEqualTo(String value) {
            addCriterion("arg_form >=", value, "argForm");
            return (Criteria) this;
        }

        public Criteria andArgFormLessThan(String value) {
            addCriterion("arg_form <", value, "argForm");
            return (Criteria) this;
        }

        public Criteria andArgFormLessThanOrEqualTo(String value) {
            addCriterion("arg_form <=", value, "argForm");
            return (Criteria) this;
        }

        public Criteria andArgFormLike(String value) {
            addCriterion("arg_form like", value, "argForm");
            return (Criteria) this;
        }

        public Criteria andArgFormNotLike(String value) {
            addCriterion("arg_form not like", value, "argForm");
            return (Criteria) this;
        }

        public Criteria andArgFormIn(List<String> values) {
            addCriterion("arg_form in", values, "argForm");
            return (Criteria) this;
        }

        public Criteria andArgFormNotIn(List<String> values) {
            addCriterion("arg_form not in", values, "argForm");
            return (Criteria) this;
        }

        public Criteria andArgFormBetween(String value1, String value2) {
            addCriterion("arg_form between", value1, value2, "argForm");
            return (Criteria) this;
        }

        public Criteria andArgFormNotBetween(String value1, String value2) {
            addCriterion("arg_form not between", value1, value2, "argForm");
            return (Criteria) this;
        }

        public Criteria andArgSampIsNull() {
            addCriterion("arg_samp is null");
            return (Criteria) this;
        }

        public Criteria andArgSampIsNotNull() {
            addCriterion("arg_samp is not null");
            return (Criteria) this;
        }

        public Criteria andArgSampEqualTo(String value) {
            addCriterion("arg_samp =", value, "argSamp");
            return (Criteria) this;
        }

        public Criteria andArgSampNotEqualTo(String value) {
            addCriterion("arg_samp <>", value, "argSamp");
            return (Criteria) this;
        }

        public Criteria andArgSampGreaterThan(String value) {
            addCriterion("arg_samp >", value, "argSamp");
            return (Criteria) this;
        }

        public Criteria andArgSampGreaterThanOrEqualTo(String value) {
            addCriterion("arg_samp >=", value, "argSamp");
            return (Criteria) this;
        }

        public Criteria andArgSampLessThan(String value) {
            addCriterion("arg_samp <", value, "argSamp");
            return (Criteria) this;
        }

        public Criteria andArgSampLessThanOrEqualTo(String value) {
            addCriterion("arg_samp <=", value, "argSamp");
            return (Criteria) this;
        }

        public Criteria andArgSampLike(String value) {
            addCriterion("arg_samp like", value, "argSamp");
            return (Criteria) this;
        }

        public Criteria andArgSampNotLike(String value) {
            addCriterion("arg_samp not like", value, "argSamp");
            return (Criteria) this;
        }

        public Criteria andArgSampIn(List<String> values) {
            addCriterion("arg_samp in", values, "argSamp");
            return (Criteria) this;
        }

        public Criteria andArgSampNotIn(List<String> values) {
            addCriterion("arg_samp not in", values, "argSamp");
            return (Criteria) this;
        }

        public Criteria andArgSampBetween(String value1, String value2) {
            addCriterion("arg_samp between", value1, value2, "argSamp");
            return (Criteria) this;
        }

        public Criteria andArgSampNotBetween(String value1, String value2) {
            addCriterion("arg_samp not between", value1, value2, "argSamp");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}