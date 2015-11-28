package cn.itcast.surveypark.domain;

import java.io.Serializable;

/**
 * BaseEntity
 */
public abstract class BaseEntity implements Serializable{

	private static final long serialVersionUID = 7778166505107977738L;

	public abstract Integer getId();

	public abstract void setId(Integer id) ;
}
