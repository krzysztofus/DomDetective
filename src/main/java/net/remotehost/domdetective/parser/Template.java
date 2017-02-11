package net.remotehost.domdetective.parser;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by Christopher on 1/30/2017.
 */
public final class Template {
    private final String name;
    private final String url;
    private final String searchPattern;
    private final String[] outputPattern;
    private final String recurrencePattern;

    public Template(String name, String url, String searchPattern, String[] outputPattern, String recurrencePattern) {
        this.name = name;
        this.url = url;
        this.searchPattern = searchPattern;
        this.outputPattern = outputPattern;
        this.recurrencePattern = recurrencePattern;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getSearchPattern() {
        return searchPattern;
    }

    public String[] getOutputPattern() {
        return outputPattern;
    }

    public String getRecurrencePattern() {
        return recurrencePattern;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Template template = (Template) o;

        return new EqualsBuilder()
                .append(name, template.name)
                .append(url, template.url)
                .append(searchPattern, template.searchPattern)
                .append(outputPattern, template.outputPattern)
                .append(recurrencePattern, template.recurrencePattern)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .append(url)
                .append(searchPattern)
                .append(outputPattern)
                .append(recurrencePattern)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("url", url)
                .append("searchPattern", searchPattern)
                .append("outputPattern", outputPattern)
                .append("recurrencePattern", recurrencePattern)
                .toString();
    }
}
