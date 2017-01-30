package net.remotehost.domdetective.parser;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.net.URL;

/**
 * Created by Christopher on 1/30/2017.
 */
public final class Case {
    private final String name;
    private final URL url;
    private final String searchPattern;
    private final String nestingPattern;

    public Case(String name, URL url, String searchPattern, String nestingPattern) {
        this.name = name;
        this.url = url;
        this.searchPattern = searchPattern;
        this.nestingPattern = nestingPattern;
    }

    public String getName() {
        return name;
    }

    public URL getUrl() {
        return url;
    }

    public String getSearchPattern() {
        return searchPattern;
    }

    public String getNestingPattern() {
        return nestingPattern;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Case aCase = (Case) o;

        return new EqualsBuilder()
                .append(name, aCase.name)
                .append(url, aCase.url)
                .append(searchPattern, aCase.searchPattern)
                .append(nestingPattern, aCase.nestingPattern)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .append(url)
                .append(searchPattern)
                .append(nestingPattern)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("url", url)
                .append("searchPattern", searchPattern)
                .append("nestingPattern", nestingPattern)
                .toString();
    }
}
