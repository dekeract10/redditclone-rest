package rs.ac.uns.ftn.redditclonesr272020.converters;

public interface Converter<M, D> {
    D toDto(M model);
}
