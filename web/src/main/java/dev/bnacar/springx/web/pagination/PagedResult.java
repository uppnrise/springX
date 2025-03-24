package dev.bnacar.springx.web.pagination;

import java.util.List;

/**
 * A generic pagination result for REST API responses.
 * Encapsulates a page of data along with pagination metadata.
 *
 * @param <T> the type of data in the page
 */
public class PagedResult<T> {

    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;

    /**
     * Default constructor.
     */
    public PagedResult() {
    }

    /**
     * Constructs a new PagedResult with the specified parameters.
     *
     * @param content the content of the page
     * @param pageNumber the page number (0-based)
     * @param pageSize the page size
     * @param totalElements the total number of elements
     */
    public PagedResult(List<T> content, int pageNumber, int pageSize, long totalElements) {
        this.content = content;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = pageSize > 0 ? (int) Math.ceil((double) totalElements / pageSize) : 0;
        this.first = pageNumber == 0;
        this.last = pageNumber >= totalPages - 1;
    }

    /**
     * Gets the content of the page.
     *
     * @return the content
     */
    public List<T> getContent() {
        return content;
    }

    /**
     * Sets the content of the page.
     *
     * @param content the content
     */
    public void setContent(List<T> content) {
        this.content = content;
    }

    /**
     * Gets the page number (0-based).
     *
     * @return the page number
     */
    public int getPageNumber() {
        return pageNumber;
    }

    /**
     * Sets the page number.
     *
     * @param pageNumber the page number
     */
    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    /**
     * Gets the page size.
     *
     * @return the page size
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * Sets the page size.
     *
     * @param pageSize the page size
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * Gets the total number of elements.
     *
     * @return the total number of elements
     */
    public long getTotalElements() {
        return totalElements;
    }

    /**
     * Sets the total number of elements.
     *
     * @param totalElements the total number of elements
     */
    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    /**
     * Gets the total number of pages.
     *
     * @return the total number of pages
     */
    public int getTotalPages() {
        return totalPages;
    }

    /**
     * Sets the total number of pages.
     *
     * @param totalPages the total number of pages
     */
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    /**
     * Gets whether this is the first page.
     *
     * @return true if this is the first page, false otherwise
     */
    public boolean isFirst() {
        return first;
    }

    /**
     * Sets whether this is the first page.
     *
     * @param first true if this is the first page, false otherwise
     */
    public void setFirst(boolean first) {
        this.first = first;
    }

    /**
     * Gets whether this is the last page.
     *
     * @return true if this is the last page, false otherwise
     */
    public boolean isLast() {
        return last;
    }

    /**
     * Sets whether this is the last page.
     *
     * @param last true if this is the last page, false otherwise
     */
    public void setLast(boolean last) {
        this.last = last;
    }
}
