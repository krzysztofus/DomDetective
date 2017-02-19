package net.remotehost.domdetective.misc;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Christopher on 2/5/2017.
 */
public class JsoupTest {

    @Test
    public void shouldFindSection() {
        //given
        final String input = "<p><div><section class=\"something\">Bajo</section><section class=\"something\">Jajo</section></div></p>";

        //when
        final Document doc = Jsoup.parse(input);
        final Elements actual = doc.select("section.something");

        //then
        assertThat(actual).isNotNull();
        assertThat(actual.size()).isEqualTo(2);
        assertThat(actual.get(0).text()).isEqualTo("Bajo");
        assertThat(actual.get(1).text()).isEqualTo("Jajo");
    }

    @Test
    public void shouldFindSpan() {
        //given
        final String input = "<table class=\"dane\">\n" +
                             " <tbody>\n" +
                             "  <tr>\n" +
                             "   <td>Numer katalogowy:</td>\n" +
                             "   <td><span>728-TY-2307</span></td>\n" +
                             "  </tr>\n" +
                             "  <tr>\n" +
                             "   <td>Nr oryginalny:</td>\n" +
                             "   <td><span>00154740</span></td>\n" +
                             "  </tr>\n" +
                             " </tbody>\n" +
                             "</table>";

        //when
        final Document doc = Jsoup.parse(input);
        final Elements actual = doc.select("table.dane > tbody > tr:has(td:contains(Numer katalogowy:)) > td > span");

        //then
        assertThat(actual).isNotNull();
        assertThat(actual.size()).isEqualTo(1);
        assertThat(actual.get(0).text()).isEqualTo("728-TY-2307");
    }

    @Test
    public void shouldFindHrefValue() {
        //given
        final String input = "<a href=\"url\" class=\"wantUrl\">I want that url</a>";

        //when
        final Document doc = Jsoup.parse(input);
        final String actual = doc.select("a.wantUrl").attr("href");

        //then
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo("url");
    }
}
