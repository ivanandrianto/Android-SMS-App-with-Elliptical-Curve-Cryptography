package com.example.ivan.sms;

import com.chibi48.sms.hash.Digest;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void sha_testcase1() throws Exception {
        Digest digest = new Digest();
        assertEquals(digest.sha1("test"), "a94a8fe5ccb19ba61c4c0873d391e987982fbbd3");
    }

    @Test
    public void sha_testcase2() throws Exception {
        Digest digest = new Digest();
        assertEquals(digest.sha1("The quick brown fox jumps over the lazy dog"), "2fd4e1c67a2d28fced849ee1bb76e7391b93eb12");
    }
}