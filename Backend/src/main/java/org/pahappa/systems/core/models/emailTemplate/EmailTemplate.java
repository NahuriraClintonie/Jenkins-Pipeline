package org.pahappa.systems.core.models.emailTemplate;

import javax.persistence.*;


import org.pahappa.systems.core.constants.TemplateType;
import org.sers.webutils.model.BaseEntity;

@Entity
@Table(name = "email_templates")
public class EmailTemplate extends BaseEntity{
    private static final long serialVersionUID = 1L;

    private TemplateType templateType;
    private String subject;
    private String template;

    @Column(name = "template_type")
    @Enumerated(EnumType.STRING)
    public TemplateType getTemplateType() {
        return templateType;
    }

    public void setTemplateType(TemplateType templateType) {
        this.templateType = templateType;
    }

    @Column(name = "subject")
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
    @Column(name = "template", columnDefinition = "TEXT")
    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
