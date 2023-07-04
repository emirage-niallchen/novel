package cn.com.emirage.novel.dao.entity;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author ${author}
 * @since 2023/05/31
 */
public class Test implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Boolean test;

    private Integer test2;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getTest() {
        return test;
    }

    public void setTest(Boolean test) {
        this.test = test;
    }

    public Integer getTest2() {
        return test2;
    }

    public void setTest2(Integer test2) {
        this.test2 = test2;
    }

    @Override
    public String toString() {
        return "Test{" +
        "id=" + id +
        ", test=" + test +
        ", test2=" + test2 +
        "}";
    }
}
