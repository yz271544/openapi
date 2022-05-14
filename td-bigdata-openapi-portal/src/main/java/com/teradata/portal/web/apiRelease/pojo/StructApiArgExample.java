package com.teradata.portal.web.apiRelease.pojo;

import java.util.ArrayList;
import java.util.List;

public class StructApiArgExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public StructApiArgExample() {
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

        public Criteria andApiIdIsNull() {
            addCriterion("api_id is null");
            return (Criteria) this;
        }

        public Criteria andApiIdIsNotNull() {
            addCriterion("api_id is not null");
            return (Criteria) this;
        }

        public Criteria andApiIdEqualTo(Integer value) {
            addCriterion("api_id =", value, "apiId");
            return (Criteria) this;
        }

        public Criteria andApiIdNotEqualTo(Integer value) {
            addCriterion("api_id <>", value, "apiId");
            return (Criteria) this;
        }

        public Criteria andApiIdGreaterThan(Integer value) {
            addCriterion("api_id >", value, "apiId");
            return (Criteria) this;
        }

        public Criteria andApiIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("api_id >=", value, "apiId");
            return (Criteria) this;
        }

        public Criteria andApiIdLessThan(Integer value) {
            addCriterion("api_id <", value, "apiId");
            return (Criteria) this;
        }

        public Criteria andApiIdLessThanOrEqualTo(Integer value) {
            addCriterion("api_id <=", value, "apiId");
            return (Criteria) this;
        }

        public Criteria andApiIdIn(List<Integer> values) {
            addCriterion("api_id in", values, "apiId");
            return (Criteria) this;
        }

        public Criteria andApiIdNotIn(List<Integer> values) {
            addCriterion("api_id not in", values, "apiId");
            return (Criteria) this;
        }

        public Criteria andApiIdBetween(Integer value1, Integer value2) {
            addCriterion("api_id between", value1, value2, "apiId");
            return (Criteria) this;
        }

        public Criteria andApiIdNotBetween(Integer value1, Integer value2) {
            addCriterion("api_id not between", value1, value2, "apiId");
            return (Criteria) this;
        }

        public Criteria andApiVersionIsNull() {
            addCriterion("api_version is null");
            return (Criteria) this;
        }

        public Criteria andApiVersionIsNotNull() {
            addCriterion("api_version is not null");
            return (Criteria) this;
        }

        public Criteria andApiVersionEqualTo(Integer value) {
            addCriterion("api_version =", value, "apiVersion");
            return (Criteria) this;
        }

        public Criteria andApiVersionNotEqualTo(Integer value) {
            addCriterion("api_version <>", value, "apiVersion");
            return (Criteria) this;
        }

        public Criteria andApiVersionGreaterThan(Integer value) {
            addCriterion("api_version >", value, "apiVersion");
            return (Criteria) this;
        }

        public Criteria andApiVersionGreaterThanOrEqualTo(Integer value) {
            addCriterion("api_version >=", value, "apiVersion");
            return (Criteria) this;
        }

        public Criteria andApiVersionLessThan(Integer value) {
            addCriterion("api_version <", value, "apiVersion");
            return (Criteria) this;
        }

        public Criteria andApiVersionLessThanOrEqualTo(Integer value) {
            addCriterion("api_version <=", value, "apiVersion");
            return (Criteria) this;
        }

        public Criteria andApiVersionIn(List<Integer> values) {
            addCriterion("api_version in", values, "apiVersion");
            return (Criteria) this;
        }

        public Criteria andApiVersionNotIn(List<Integer> values) {
            addCriterion("api_version not in", values, "apiVersion");
            return (Criteria) this;
        }

        public Criteria andApiVersionBetween(Integer value1, Integer value2) {
            addCriterion("api_version between", value1, value2, "apiVersion");
            return (Criteria) this;
        }

        public Criteria andApiVersionNotBetween(Integer value1, Integer value2) {
            addCriterion("api_version not between", value1, value2, "apiVersion");
            return (Criteria) this;
        }

        public Criteria andFieldAliasIsNull() {
            addCriterion("field_alias is null");
            return (Criteria) this;
        }

        public Criteria andFieldAliasIsNotNull() {
            addCriterion("field_alias is not null");
            return (Criteria) this;
        }

        public Criteria andFieldAliasEqualTo(String value) {
            addCriterion("field_alias =", value, "fieldAlias");
            return (Criteria) this;
        }

        public Criteria andFieldAliasNotEqualTo(String value) {
            addCriterion("field_alias <>", value, "fieldAlias");
            return (Criteria) this;
        }

        public Criteria andFieldAliasGreaterThan(String value) {
            addCriterion("field_alias >", value, "fieldAlias");
            return (Criteria) this;
        }

        public Criteria andFieldAliasGreaterThanOrEqualTo(String value) {
            addCriterion("field_alias >=", value, "fieldAlias");
            return (Criteria) this;
        }

        public Criteria andFieldAliasLessThan(String value) {
            addCriterion("field_alias <", value, "fieldAlias");
            return (Criteria) this;
        }

        public Criteria andFieldAliasLessThanOrEqualTo(String value) {
            addCriterion("field_alias <=", value, "fieldAlias");
            return (Criteria) this;
        }

        public Criteria andFieldAliasLike(String value) {
            addCriterion("field_alias like", value, "fieldAlias");
            return (Criteria) this;
        }

        public Criteria andFieldAliasNotLike(String value) {
            addCriterion("field_alias not like", value, "fieldAlias");
            return (Criteria) this;
        }

        public Criteria andFieldAliasIn(List<String> values) {
            addCriterion("field_alias in", values, "fieldAlias");
            return (Criteria) this;
        }

        public Criteria andFieldAliasNotIn(List<String> values) {
            addCriterion("field_alias not in", values, "fieldAlias");
            return (Criteria) this;
        }

        public Criteria andFieldAliasBetween(String value1, String value2) {
            addCriterion("field_alias between", value1, value2, "fieldAlias");
            return (Criteria) this;
        }

        public Criteria andFieldAliasNotBetween(String value1, String value2) {
            addCriterion("field_alias not between", value1, value2, "fieldAlias");
            return (Criteria) this;
        }

        public Criteria andFieldNameIsNull() {
            addCriterion("field_name is null");
            return (Criteria) this;
        }

        public Criteria andFieldNameIsNotNull() {
            addCriterion("field_name is not null");
            return (Criteria) this;
        }

        public Criteria andFieldNameEqualTo(String value) {
            addCriterion("field_name =", value, "fieldName");
            return (Criteria) this;
        }

        public Criteria andFieldNameNotEqualTo(String value) {
            addCriterion("field_name <>", value, "fieldName");
            return (Criteria) this;
        }

        public Criteria andFieldNameGreaterThan(String value) {
            addCriterion("field_name >", value, "fieldName");
            return (Criteria) this;
        }

        public Criteria andFieldNameGreaterThanOrEqualTo(String value) {
            addCriterion("field_name >=", value, "fieldName");
            return (Criteria) this;
        }

        public Criteria andFieldNameLessThan(String value) {
            addCriterion("field_name <", value, "fieldName");
            return (Criteria) this;
        }

        public Criteria andFieldNameLessThanOrEqualTo(String value) {
            addCriterion("field_name <=", value, "fieldName");
            return (Criteria) this;
        }

        public Criteria andFieldNameLike(String value) {
            addCriterion("field_name like", value, "fieldName");
            return (Criteria) this;
        }

        public Criteria andFieldNameNotLike(String value) {
            addCriterion("field_name not like", value, "fieldName");
            return (Criteria) this;
        }

        public Criteria andFieldNameIn(List<String> values) {
            addCriterion("field_name in", values, "fieldName");
            return (Criteria) this;
        }

        public Criteria andFieldNameNotIn(List<String> values) {
            addCriterion("field_name not in", values, "fieldName");
            return (Criteria) this;
        }

        public Criteria andFieldNameBetween(String value1, String value2) {
            addCriterion("field_name between", value1, value2, "fieldName");
            return (Criteria) this;
        }

        public Criteria andFieldNameNotBetween(String value1, String value2) {
            addCriterion("field_name not between", value1, value2, "fieldName");
            return (Criteria) this;
        }

        public Criteria andFieldEffStatIsNull() {
            addCriterion("field_eff_stat is null");
            return (Criteria) this;
        }

        public Criteria andFieldEffStatIsNotNull() {
            addCriterion("field_eff_stat is not null");
            return (Criteria) this;
        }

        public Criteria andFieldEffStatEqualTo(Integer value) {
            addCriterion("field_eff_stat =", value, "fieldEffStat");
            return (Criteria) this;
        }

        public Criteria andFieldEffStatNotEqualTo(Integer value) {
            addCriterion("field_eff_stat <>", value, "fieldEffStat");
            return (Criteria) this;
        }

        public Criteria andFieldEffStatGreaterThan(Integer value) {
            addCriterion("field_eff_stat >", value, "fieldEffStat");
            return (Criteria) this;
        }

        public Criteria andFieldEffStatGreaterThanOrEqualTo(Integer value) {
            addCriterion("field_eff_stat >=", value, "fieldEffStat");
            return (Criteria) this;
        }

        public Criteria andFieldEffStatLessThan(Integer value) {
            addCriterion("field_eff_stat <", value, "fieldEffStat");
            return (Criteria) this;
        }

        public Criteria andFieldEffStatLessThanOrEqualTo(Integer value) {
            addCriterion("field_eff_stat <=", value, "fieldEffStat");
            return (Criteria) this;
        }

        public Criteria andFieldEffStatIn(List<Integer> values) {
            addCriterion("field_eff_stat in", values, "fieldEffStat");
            return (Criteria) this;
        }

        public Criteria andFieldEffStatNotIn(List<Integer> values) {
            addCriterion("field_eff_stat not in", values, "fieldEffStat");
            return (Criteria) this;
        }

        public Criteria andFieldEffStatBetween(Integer value1, Integer value2) {
            addCriterion("field_eff_stat between", value1, value2, "fieldEffStat");
            return (Criteria) this;
        }

        public Criteria andFieldEffStatNotBetween(Integer value1, Integer value2) {
            addCriterion("field_eff_stat not between", value1, value2, "fieldEffStat");
            return (Criteria) this;
        }

        public Criteria andFieldSorcTypeIsNull() {
            addCriterion("field_sorc_type is null");
            return (Criteria) this;
        }

        public Criteria andFieldSorcTypeIsNotNull() {
            addCriterion("field_sorc_type is not null");
            return (Criteria) this;
        }

        public Criteria andFieldSorcTypeEqualTo(String value) {
            addCriterion("field_sorc_type =", value, "fieldSorcType");
            return (Criteria) this;
        }

        public Criteria andFieldSorcTypeNotEqualTo(String value) {
            addCriterion("field_sorc_type <>", value, "fieldSorcType");
            return (Criteria) this;
        }

        public Criteria andFieldSorcTypeGreaterThan(String value) {
            addCriterion("field_sorc_type >", value, "fieldSorcType");
            return (Criteria) this;
        }

        public Criteria andFieldSorcTypeGreaterThanOrEqualTo(String value) {
            addCriterion("field_sorc_type >=", value, "fieldSorcType");
            return (Criteria) this;
        }

        public Criteria andFieldSorcTypeLessThan(String value) {
            addCriterion("field_sorc_type <", value, "fieldSorcType");
            return (Criteria) this;
        }

        public Criteria andFieldSorcTypeLessThanOrEqualTo(String value) {
            addCriterion("field_sorc_type <=", value, "fieldSorcType");
            return (Criteria) this;
        }

        public Criteria andFieldSorcTypeLike(String value) {
            addCriterion("field_sorc_type like", value, "fieldSorcType");
            return (Criteria) this;
        }

        public Criteria andFieldSorcTypeNotLike(String value) {
            addCriterion("field_sorc_type not like", value, "fieldSorcType");
            return (Criteria) this;
        }

        public Criteria andFieldSorcTypeIn(List<String> values) {
            addCriterion("field_sorc_type in", values, "fieldSorcType");
            return (Criteria) this;
        }

        public Criteria andFieldSorcTypeNotIn(List<String> values) {
            addCriterion("field_sorc_type not in", values, "fieldSorcType");
            return (Criteria) this;
        }

        public Criteria andFieldSorcTypeBetween(String value1, String value2) {
            addCriterion("field_sorc_type between", value1, value2, "fieldSorcType");
            return (Criteria) this;
        }

        public Criteria andFieldSorcTypeNotBetween(String value1, String value2) {
            addCriterion("field_sorc_type not between", value1, value2, "fieldSorcType");
            return (Criteria) this;
        }

        public Criteria andFieldTargtTypeIsNull() {
            addCriterion("field_targt_type is null");
            return (Criteria) this;
        }

        public Criteria andFieldTargtTypeIsNotNull() {
            addCriterion("field_targt_type is not null");
            return (Criteria) this;
        }

        public Criteria andFieldTargtTypeEqualTo(String value) {
            addCriterion("field_targt_type =", value, "fieldTargtType");
            return (Criteria) this;
        }

        public Criteria andFieldTargtTypeNotEqualTo(String value) {
            addCriterion("field_targt_type <>", value, "fieldTargtType");
            return (Criteria) this;
        }

        public Criteria andFieldTargtTypeGreaterThan(String value) {
            addCriterion("field_targt_type >", value, "fieldTargtType");
            return (Criteria) this;
        }

        public Criteria andFieldTargtTypeGreaterThanOrEqualTo(String value) {
            addCriterion("field_targt_type >=", value, "fieldTargtType");
            return (Criteria) this;
        }

        public Criteria andFieldTargtTypeLessThan(String value) {
            addCriterion("field_targt_type <", value, "fieldTargtType");
            return (Criteria) this;
        }

        public Criteria andFieldTargtTypeLessThanOrEqualTo(String value) {
            addCriterion("field_targt_type <=", value, "fieldTargtType");
            return (Criteria) this;
        }

        public Criteria andFieldTargtTypeLike(String value) {
            addCriterion("field_targt_type like", value, "fieldTargtType");
            return (Criteria) this;
        }

        public Criteria andFieldTargtTypeNotLike(String value) {
            addCriterion("field_targt_type not like", value, "fieldTargtType");
            return (Criteria) this;
        }

        public Criteria andFieldTargtTypeIn(List<String> values) {
            addCriterion("field_targt_type in", values, "fieldTargtType");
            return (Criteria) this;
        }

        public Criteria andFieldTargtTypeNotIn(List<String> values) {
            addCriterion("field_targt_type not in", values, "fieldTargtType");
            return (Criteria) this;
        }

        public Criteria andFieldTargtTypeBetween(String value1, String value2) {
            addCriterion("field_targt_type between", value1, value2, "fieldTargtType");
            return (Criteria) this;
        }

        public Criteria andFieldTargtTypeNotBetween(String value1, String value2) {
            addCriterion("field_targt_type not between", value1, value2, "fieldTargtType");
            return (Criteria) this;
        }

        public Criteria andFieldTitleIsNull() {
            addCriterion("field_title is null");
            return (Criteria) this;
        }

        public Criteria andFieldTitleIsNotNull() {
            addCriterion("field_title is not null");
            return (Criteria) this;
        }

        public Criteria andFieldTitleEqualTo(String value) {
            addCriterion("field_title =", value, "fieldTitle");
            return (Criteria) this;
        }

        public Criteria andFieldTitleNotEqualTo(String value) {
            addCriterion("field_title <>", value, "fieldTitle");
            return (Criteria) this;
        }

        public Criteria andFieldTitleGreaterThan(String value) {
            addCriterion("field_title >", value, "fieldTitle");
            return (Criteria) this;
        }

        public Criteria andFieldTitleGreaterThanOrEqualTo(String value) {
            addCriterion("field_title >=", value, "fieldTitle");
            return (Criteria) this;
        }

        public Criteria andFieldTitleLessThan(String value) {
            addCriterion("field_title <", value, "fieldTitle");
            return (Criteria) this;
        }

        public Criteria andFieldTitleLessThanOrEqualTo(String value) {
            addCriterion("field_title <=", value, "fieldTitle");
            return (Criteria) this;
        }

        public Criteria andFieldTitleLike(String value) {
            addCriterion("field_title like", value, "fieldTitle");
            return (Criteria) this;
        }

        public Criteria andFieldTitleNotLike(String value) {
            addCriterion("field_title not like", value, "fieldTitle");
            return (Criteria) this;
        }

        public Criteria andFieldTitleIn(List<String> values) {
            addCriterion("field_title in", values, "fieldTitle");
            return (Criteria) this;
        }

        public Criteria andFieldTitleNotIn(List<String> values) {
            addCriterion("field_title not in", values, "fieldTitle");
            return (Criteria) this;
        }

        public Criteria andFieldTitleBetween(String value1, String value2) {
            addCriterion("field_title between", value1, value2, "fieldTitle");
            return (Criteria) this;
        }

        public Criteria andFieldTitleNotBetween(String value1, String value2) {
            addCriterion("field_title not between", value1, value2, "fieldTitle");
            return (Criteria) this;
        }

        public Criteria andMustTypeIsNull() {
            addCriterion("must_type is null");
            return (Criteria) this;
        }

        public Criteria andMustTypeIsNotNull() {
            addCriterion("must_type is not null");
            return (Criteria) this;
        }

        public Criteria andMustTypeEqualTo(Integer value) {
            addCriterion("must_type =", value, "mustType");
            return (Criteria) this;
        }

        public Criteria andMustTypeNotEqualTo(Integer value) {
            addCriterion("must_type <>", value, "mustType");
            return (Criteria) this;
        }

        public Criteria andMustTypeGreaterThan(Integer value) {
            addCriterion("must_type >", value, "mustType");
            return (Criteria) this;
        }

        public Criteria andMustTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("must_type >=", value, "mustType");
            return (Criteria) this;
        }

        public Criteria andMustTypeLessThan(Integer value) {
            addCriterion("must_type <", value, "mustType");
            return (Criteria) this;
        }

        public Criteria andMustTypeLessThanOrEqualTo(Integer value) {
            addCriterion("must_type <=", value, "mustType");
            return (Criteria) this;
        }

        public Criteria andMustTypeIn(List<Integer> values) {
            addCriterion("must_type in", values, "mustType");
            return (Criteria) this;
        }

        public Criteria andMustTypeNotIn(List<Integer> values) {
            addCriterion("must_type not in", values, "mustType");
            return (Criteria) this;
        }

        public Criteria andMustTypeBetween(Integer value1, Integer value2) {
            addCriterion("must_type between", value1, value2, "mustType");
            return (Criteria) this;
        }

        public Criteria andMustTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("must_type not between", value1, value2, "mustType");
            return (Criteria) this;
        }

        public Criteria andMustOneGrpIdIsNull() {
            addCriterion("must_one_grp_id is null");
            return (Criteria) this;
        }

        public Criteria andMustOneGrpIdIsNotNull() {
            addCriterion("must_one_grp_id is not null");
            return (Criteria) this;
        }

        public Criteria andMustOneGrpIdEqualTo(Integer value) {
            addCriterion("must_one_grp_id =", value, "mustOneGrpId");
            return (Criteria) this;
        }

        public Criteria andMustOneGrpIdNotEqualTo(Integer value) {
            addCriterion("must_one_grp_id <>", value, "mustOneGrpId");
            return (Criteria) this;
        }

        public Criteria andMustOneGrpIdGreaterThan(Integer value) {
            addCriterion("must_one_grp_id >", value, "mustOneGrpId");
            return (Criteria) this;
        }

        public Criteria andMustOneGrpIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("must_one_grp_id >=", value, "mustOneGrpId");
            return (Criteria) this;
        }

        public Criteria andMustOneGrpIdLessThan(Integer value) {
            addCriterion("must_one_grp_id <", value, "mustOneGrpId");
            return (Criteria) this;
        }

        public Criteria andMustOneGrpIdLessThanOrEqualTo(Integer value) {
            addCriterion("must_one_grp_id <=", value, "mustOneGrpId");
            return (Criteria) this;
        }

        public Criteria andMustOneGrpIdIn(List<Integer> values) {
            addCriterion("must_one_grp_id in", values, "mustOneGrpId");
            return (Criteria) this;
        }

        public Criteria andMustOneGrpIdNotIn(List<Integer> values) {
            addCriterion("must_one_grp_id not in", values, "mustOneGrpId");
            return (Criteria) this;
        }

        public Criteria andMustOneGrpIdBetween(Integer value1, Integer value2) {
            addCriterion("must_one_grp_id between", value1, value2, "mustOneGrpId");
            return (Criteria) this;
        }

        public Criteria andMustOneGrpIdNotBetween(Integer value1, Integer value2) {
            addCriterion("must_one_grp_id not between", value1, value2, "mustOneGrpId");
            return (Criteria) this;
        }

        public Criteria andReqArgIdIsNull() {
            addCriterion("req_arg_id is null");
            return (Criteria) this;
        }

        public Criteria andReqArgIdIsNotNull() {
            addCriterion("req_arg_id is not null");
            return (Criteria) this;
        }

        public Criteria andReqArgIdEqualTo(String value) {
            addCriterion("req_arg_id =", value, "reqArgId");
            return (Criteria) this;
        }

        public Criteria andReqArgIdNotEqualTo(String value) {
            addCriterion("req_arg_id <>", value, "reqArgId");
            return (Criteria) this;
        }

        public Criteria andReqArgIdGreaterThan(String value) {
            addCriterion("req_arg_id >", value, "reqArgId");
            return (Criteria) this;
        }

        public Criteria andReqArgIdGreaterThanOrEqualTo(String value) {
            addCriterion("req_arg_id >=", value, "reqArgId");
            return (Criteria) this;
        }

        public Criteria andReqArgIdLessThan(String value) {
            addCriterion("req_arg_id <", value, "reqArgId");
            return (Criteria) this;
        }

        public Criteria andReqArgIdLessThanOrEqualTo(String value) {
            addCriterion("req_arg_id <=", value, "reqArgId");
            return (Criteria) this;
        }

        public Criteria andReqArgIdLike(String value) {
            addCriterion("req_arg_id like", value, "reqArgId");
            return (Criteria) this;
        }

        public Criteria andReqArgIdNotLike(String value) {
            addCriterion("req_arg_id not like", value, "reqArgId");
            return (Criteria) this;
        }

        public Criteria andReqArgIdIn(List<String> values) {
            addCriterion("req_arg_id in", values, "reqArgId");
            return (Criteria) this;
        }

        public Criteria andReqArgIdNotIn(List<String> values) {
            addCriterion("req_arg_id not in", values, "reqArgId");
            return (Criteria) this;
        }

        public Criteria andReqArgIdBetween(String value1, String value2) {
            addCriterion("req_arg_id between", value1, value2, "reqArgId");
            return (Criteria) this;
        }

        public Criteria andReqArgIdNotBetween(String value1, String value2) {
            addCriterion("req_arg_id not between", value1, value2, "reqArgId");
            return (Criteria) this;
        }

        public Criteria andReqArgDefltValIsNull() {
            addCriterion("req_arg_deflt_val is null");
            return (Criteria) this;
        }

        public Criteria andReqArgDefltValIsNotNull() {
            addCriterion("req_arg_deflt_val is not null");
            return (Criteria) this;
        }

        public Criteria andReqArgDefltValEqualTo(String value) {
            addCriterion("req_arg_deflt_val =", value, "reqArgDefltVal");
            return (Criteria) this;
        }

        public Criteria andReqArgDefltValNotEqualTo(String value) {
            addCriterion("req_arg_deflt_val <>", value, "reqArgDefltVal");
            return (Criteria) this;
        }

        public Criteria andReqArgDefltValGreaterThan(String value) {
            addCriterion("req_arg_deflt_val >", value, "reqArgDefltVal");
            return (Criteria) this;
        }

        public Criteria andReqArgDefltValGreaterThanOrEqualTo(String value) {
            addCriterion("req_arg_deflt_val >=", value, "reqArgDefltVal");
            return (Criteria) this;
        }

        public Criteria andReqArgDefltValLessThan(String value) {
            addCriterion("req_arg_deflt_val <", value, "reqArgDefltVal");
            return (Criteria) this;
        }

        public Criteria andReqArgDefltValLessThanOrEqualTo(String value) {
            addCriterion("req_arg_deflt_val <=", value, "reqArgDefltVal");
            return (Criteria) this;
        }

        public Criteria andReqArgDefltValLike(String value) {
            addCriterion("req_arg_deflt_val like", value, "reqArgDefltVal");
            return (Criteria) this;
        }

        public Criteria andReqArgDefltValNotLike(String value) {
            addCriterion("req_arg_deflt_val not like", value, "reqArgDefltVal");
            return (Criteria) this;
        }

        public Criteria andReqArgDefltValIn(List<String> values) {
            addCriterion("req_arg_deflt_val in", values, "reqArgDefltVal");
            return (Criteria) this;
        }

        public Criteria andReqArgDefltValNotIn(List<String> values) {
            addCriterion("req_arg_deflt_val not in", values, "reqArgDefltVal");
            return (Criteria) this;
        }

        public Criteria andReqArgDefltValBetween(String value1, String value2) {
            addCriterion("req_arg_deflt_val between", value1, value2, "reqArgDefltVal");
            return (Criteria) this;
        }

        public Criteria andReqArgDefltValNotBetween(String value1, String value2) {
            addCriterion("req_arg_deflt_val not between", value1, value2, "reqArgDefltVal");
            return (Criteria) this;
        }

        public Criteria andRespnArgIdIsNull() {
            addCriterion("respn_arg_id is null");
            return (Criteria) this;
        }

        public Criteria andRespnArgIdIsNotNull() {
            addCriterion("respn_arg_id is not null");
            return (Criteria) this;
        }

        public Criteria andRespnArgIdEqualTo(String value) {
            addCriterion("respn_arg_id =", value, "respnArgId");
            return (Criteria) this;
        }

        public Criteria andRespnArgIdNotEqualTo(String value) {
            addCriterion("respn_arg_id <>", value, "respnArgId");
            return (Criteria) this;
        }

        public Criteria andRespnArgIdGreaterThan(String value) {
            addCriterion("respn_arg_id >", value, "respnArgId");
            return (Criteria) this;
        }

        public Criteria andRespnArgIdGreaterThanOrEqualTo(String value) {
            addCriterion("respn_arg_id >=", value, "respnArgId");
            return (Criteria) this;
        }

        public Criteria andRespnArgIdLessThan(String value) {
            addCriterion("respn_arg_id <", value, "respnArgId");
            return (Criteria) this;
        }

        public Criteria andRespnArgIdLessThanOrEqualTo(String value) {
            addCriterion("respn_arg_id <=", value, "respnArgId");
            return (Criteria) this;
        }

        public Criteria andRespnArgIdLike(String value) {
            addCriterion("respn_arg_id like", value, "respnArgId");
            return (Criteria) this;
        }

        public Criteria andRespnArgIdNotLike(String value) {
            addCriterion("respn_arg_id not like", value, "respnArgId");
            return (Criteria) this;
        }

        public Criteria andRespnArgIdIn(List<String> values) {
            addCriterion("respn_arg_id in", values, "respnArgId");
            return (Criteria) this;
        }

        public Criteria andRespnArgIdNotIn(List<String> values) {
            addCriterion("respn_arg_id not in", values, "respnArgId");
            return (Criteria) this;
        }

        public Criteria andRespnArgIdBetween(String value1, String value2) {
            addCriterion("respn_arg_id between", value1, value2, "respnArgId");
            return (Criteria) this;
        }

        public Criteria andRespnArgIdNotBetween(String value1, String value2) {
            addCriterion("respn_arg_id not between", value1, value2, "respnArgId");
            return (Criteria) this;
        }

        public Criteria andRespnArgSampValIsNull() {
            addCriterion("respn_arg_samp_val is null");
            return (Criteria) this;
        }

        public Criteria andRespnArgSampValIsNotNull() {
            addCriterion("respn_arg_samp_val is not null");
            return (Criteria) this;
        }

        public Criteria andRespnArgSampValEqualTo(String value) {
            addCriterion("respn_arg_samp_val =", value, "respnArgSampVal");
            return (Criteria) this;
        }

        public Criteria andRespnArgSampValNotEqualTo(String value) {
            addCriterion("respn_arg_samp_val <>", value, "respnArgSampVal");
            return (Criteria) this;
        }

        public Criteria andRespnArgSampValGreaterThan(String value) {
            addCriterion("respn_arg_samp_val >", value, "respnArgSampVal");
            return (Criteria) this;
        }

        public Criteria andRespnArgSampValGreaterThanOrEqualTo(String value) {
            addCriterion("respn_arg_samp_val >=", value, "respnArgSampVal");
            return (Criteria) this;
        }

        public Criteria andRespnArgSampValLessThan(String value) {
            addCriterion("respn_arg_samp_val <", value, "respnArgSampVal");
            return (Criteria) this;
        }

        public Criteria andRespnArgSampValLessThanOrEqualTo(String value) {
            addCriterion("respn_arg_samp_val <=", value, "respnArgSampVal");
            return (Criteria) this;
        }

        public Criteria andRespnArgSampValLike(String value) {
            addCriterion("respn_arg_samp_val like", value, "respnArgSampVal");
            return (Criteria) this;
        }

        public Criteria andRespnArgSampValNotLike(String value) {
            addCriterion("respn_arg_samp_val not like", value, "respnArgSampVal");
            return (Criteria) this;
        }

        public Criteria andRespnArgSampValIn(List<String> values) {
            addCriterion("respn_arg_samp_val in", values, "respnArgSampVal");
            return (Criteria) this;
        }

        public Criteria andRespnArgSampValNotIn(List<String> values) {
            addCriterion("respn_arg_samp_val not in", values, "respnArgSampVal");
            return (Criteria) this;
        }

        public Criteria andRespnArgSampValBetween(String value1, String value2) {
            addCriterion("respn_arg_samp_val between", value1, value2, "respnArgSampVal");
            return (Criteria) this;
        }

        public Criteria andRespnArgSampValNotBetween(String value1, String value2) {
            addCriterion("respn_arg_samp_val not between", value1, value2, "respnArgSampVal");
            return (Criteria) this;
        }

        public Criteria andFieldFileDescIsNull() {
            addCriterion("field_file_desc is null");
            return (Criteria) this;
        }

        public Criteria andFieldFileDescIsNotNull() {
            addCriterion("field_file_desc is not null");
            return (Criteria) this;
        }

        public Criteria andFieldFileDescEqualTo(String value) {
            addCriterion("field_file_desc =", value, "fieldFileDesc");
            return (Criteria) this;
        }

        public Criteria andFieldFileDescNotEqualTo(String value) {
            addCriterion("field_file_desc <>", value, "fieldFileDesc");
            return (Criteria) this;
        }

        public Criteria andFieldFileDescGreaterThan(String value) {
            addCriterion("field_file_desc >", value, "fieldFileDesc");
            return (Criteria) this;
        }

        public Criteria andFieldFileDescGreaterThanOrEqualTo(String value) {
            addCriterion("field_file_desc >=", value, "fieldFileDesc");
            return (Criteria) this;
        }

        public Criteria andFieldFileDescLessThan(String value) {
            addCriterion("field_file_desc <", value, "fieldFileDesc");
            return (Criteria) this;
        }

        public Criteria andFieldFileDescLessThanOrEqualTo(String value) {
            addCriterion("field_file_desc <=", value, "fieldFileDesc");
            return (Criteria) this;
        }

        public Criteria andFieldFileDescLike(String value) {
            addCriterion("field_file_desc like", value, "fieldFileDesc");
            return (Criteria) this;
        }

        public Criteria andFieldFileDescNotLike(String value) {
            addCriterion("field_file_desc not like", value, "fieldFileDesc");
            return (Criteria) this;
        }

        public Criteria andFieldFileDescIn(List<String> values) {
            addCriterion("field_file_desc in", values, "fieldFileDesc");
            return (Criteria) this;
        }

        public Criteria andFieldFileDescNotIn(List<String> values) {
            addCriterion("field_file_desc not in", values, "fieldFileDesc");
            return (Criteria) this;
        }

        public Criteria andFieldFileDescBetween(String value1, String value2) {
            addCriterion("field_file_desc between", value1, value2, "fieldFileDesc");
            return (Criteria) this;
        }

        public Criteria andFieldFileDescNotBetween(String value1, String value2) {
            addCriterion("field_file_desc not between", value1, value2, "fieldFileDesc");
            return (Criteria) this;
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

        public Criteria andValueRangeIsNull() {
            addCriterion("value_range is null");
            return (Criteria) this;
        }

        public Criteria andValueRangeIsNotNull() {
            addCriterion("value_range is not null");
            return (Criteria) this;
        }

        public Criteria andValueRangeEqualTo(String value) {
            addCriterion("value_range =", value, "valueRange");
            return (Criteria) this;
        }

        public Criteria andValueRangeNotEqualTo(String value) {
            addCriterion("value_range <>", value, "valueRange");
            return (Criteria) this;
        }

        public Criteria andValueRangeGreaterThan(String value) {
            addCriterion("value_range >", value, "valueRange");
            return (Criteria) this;
        }

        public Criteria andValueRangeGreaterThanOrEqualTo(String value) {
            addCriterion("value_range >=", value, "valueRange");
            return (Criteria) this;
        }

        public Criteria andValueRangeLessThan(String value) {
            addCriterion("value_range <", value, "valueRange");
            return (Criteria) this;
        }

        public Criteria andValueRangeLessThanOrEqualTo(String value) {
            addCriterion("value_range <=", value, "valueRange");
            return (Criteria) this;
        }

        public Criteria andValueRangeLike(String value) {
            addCriterion("value_range like", value, "valueRange");
            return (Criteria) this;
        }

        public Criteria andValueRangeNotLike(String value) {
            addCriterion("value_range not like", value, "valueRange");
            return (Criteria) this;
        }

        public Criteria andValueRangeIn(List<String> values) {
            addCriterion("value_range in", values, "valueRange");
            return (Criteria) this;
        }

        public Criteria andValueRangeNotIn(List<String> values) {
            addCriterion("value_range not in", values, "valueRange");
            return (Criteria) this;
        }

        public Criteria andValueRangeBetween(String value1, String value2) {
            addCriterion("value_range between", value1, value2, "valueRange");
            return (Criteria) this;
        }

        public Criteria andValueRangeNotBetween(String value1, String value2) {
            addCriterion("value_range not between", value1, value2, "valueRange");
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