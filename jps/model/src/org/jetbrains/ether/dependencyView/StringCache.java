package org.jetbrains.ether.dependencyView;

import com.intellij.util.io.KeyDescriptor;
import org.jetbrains.ether.RW;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: db
 * Date: 14.02.11
 * Time: 2:03
 * To change this template use File | Settings | File Templates.
 */
public class StringCache {

  public static class S implements Comparable<S>, RW.Writable, KeyDescriptor<S> {
    public final int index;
    public final String value;

    @Override
    public void save(final DataOutput out, final S value) throws IOException {
      out.writeUTF(value.value);
    }

    @Override
    public S read(final DataInput in) throws IOException {
      return get(in.readUTF());
    }

    @Override
    public int getHashCode(final S value) {
      return value.hashCode();
    }

    @Override
    public boolean isEqual(final S val1, final S val2) {
      return val1.equals(val2);
    }

    private S(final int i, final String v) {
      index = i;
      value = v;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      S s = (S)o;

      if (index != s.index) return false;

      return true;
    }

    @Override
    public int hashCode() {
      return index;
    }

    public int compareTo(S o) {
      return index - o.index;
    }

    public void write(BufferedWriter w) {
      RW.writeln(w, value);
    }

    public static final RW.Reader<S> reader = new RW.Reader<S>() {
      public S read(final BufferedReader r) {
        final String s = RW.readString(r);
        return StringCache.get(s);
      }
    };

    public String toString() {
      return value;
    }
  }

  private static final Map<String, S> map = new HashMap<String, S>();
  private static int index = 0;

  public static S get(final String s) {
//        if (s == null)
//            return null;
//
    S r = map.get(s);

    if (r == null) {
      r = new S(index++, s);
      map.put(s, r);
    }

    return r;
  }

  public static S[] get(final String[] s) {
    if (s == null) {
      return null;
    }

    final S[] r = new S[s.length];

    for (int i = 0; i < r.length; i++) {
      r[i] = get(s[i]);
    }

    return r;
  }

  public static RW.ToWritable<S> fromS = new RW.ToWritable<S>() {
    public RW.Writable convert(S x) {
      return RW.fromString.convert(x.value);
    }
  };

  public static RW.Reader<S> reader = new RW.Reader<S>() {
    public S read(final BufferedReader r) {
      try {
        return get(r.readLine());
      }
      catch (IOException e) {
        e.printStackTrace();
        return null;
      }
    }
  };

}
