package com.youngdatafan.portal.model.management.forward.superset;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : description</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2019/12/26 2:41 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
public class TableColumns {
    private String name;

    private String agg;

    private String type;

    private Boolean is_date = false;

    private Boolean is_dim = false;

    private String verbose_name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAgg() {
        return agg;
    }

    public void setAgg(String agg) {
        this.agg = agg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getIs_date() {
        return is_date;
    }

    public void setIs_date(Boolean is_date) {
        this.is_date = is_date;
    }

    public Boolean getIs_dim() {
        return is_dim;
    }

    public void setIs_dim(Boolean is_dim) {
        this.is_dim = is_dim;
    }


    public String getVerbose_name() {
        return verbose_name;
    }

    public void setVerbose_name(String verbose_name) {
        this.verbose_name = verbose_name;
    }

    @Override
    public String toString() {
        return "{\"name\":\"" + name + "\",\"agg\":\"" + agg + "\",\"type\":\"" + type + "\",\"is_date\":" + is_date + ",\"is_dim\":" + is_dim + ",\"verbose_name\":\"" + verbose_name + "\"}"
                ;

    }

}
