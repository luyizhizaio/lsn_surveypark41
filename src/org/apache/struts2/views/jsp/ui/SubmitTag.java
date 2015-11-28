package org.apache.struts2.views.jsp.ui;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.struts2.components.Component;
import org.apache.struts2.components.Submit;

import cn.itcast.surveypark.util.ValidateUtil;

import com.opensymphony.xwork2.util.ValueStack;

/**
 * @see Submit
 */
public class SubmitTag extends AbstractClosingTag {

    private static final long serialVersionUID = 2179281109958301343L;

    protected String action;
    protected String method;
    protected String align;
    protected String type;
    protected String src;

    public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        return new Submit(stack, req, res);
    }

    protected void populateParams() {
        super.populateParams();

        Submit submit = ((Submit) component);
        submit.setAction(action);
        submit.setMethod(method);
        submit.setAlign(align);
        submit.setType(type);
        submit.setSrc(src);
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSrc(String src) {
        this.src = src;
    }
    
	public int doEndTag() throws JspException {
		String ac = getValidActionName() ;
		if(ac.contains("?")){
			ac =ac.substring(0, ac.indexOf("?")); 
		}
		if(ValidateUtil.hasRight(ac, getParentFormTag().namespace, (HttpServletRequest)pageContext.getRequest(), null)){
			return super.doEndTag();
		}
		else{
			return SKIP_BODY ;
		}
	}

	public int doStartTag() throws JspException {
		String ac = getValidActionName() ;
		if(ac.contains("?")){
			ac =ac.substring(0, ac.indexOf("?")); 
		}
		if(ValidateUtil.hasRight(ac, getParentFormTag().namespace, (HttpServletRequest)pageContext.getRequest(), null)){
			return super.doStartTag();
		}
		else{
			return SKIP_BODY ;
		}
	}
    
	/**
	 * 取得有效地actionName
	 * 1.自身带action,返回action
	 * 2.自身不带action,返回form action.
	 */
	private String getValidActionName(){
		if(ValidateUtil.isValid(action)){
			return action ;
		}
		else{
			FormTag formTag = getParentFormTag();
			return formTag.action;
		}
	}

	/**
	 * 取得上级(上级的上级)标签是FormTag的对象
	 */
	private FormTag getParentFormTag() {
		Tag tag = this.getParent() ;
		while(tag != null){
			if(tag instanceof FormTag){
				break;
			}
			else{
				tag = tag.getParent();
			}
		}
		return (FormTag)tag;
	}
    
}
