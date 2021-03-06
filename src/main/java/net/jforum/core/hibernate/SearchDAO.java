/*
 * Copyright (c) JForum Team. All rights reserved.
 *
 * The software in this package is published under the terms of the LGPL
 * license a copy of which has been included with this distribution in the
 * license.txt file.
 *
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.core.hibernate;

import net.jforum.entities.Post;
import net.jforum.entities.util.SearchParams;
import net.jforum.entities.util.SearchResult;
import net.jforum.entities.util.SearchSort;
import net.jforum.entities.util.SearchSortType;
import net.jforum.repository.SearchRepository;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.Search;

/**
 * @author Rafael Steil
 */
public class SearchDAO implements SearchRepository {
	private SessionFactory sessionFactory;

	public SearchDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("unchecked")
	public SearchResult search(SearchParams params) throws ParseException {
		String buildQuery = params.buildQuery();
		FullTextQuery query = this.createQuery(buildQuery);

		query.setFirstResult(params.getStart());
		query.setMaxResults(params.getMaxResults());
		query.setFetchSize(params.getMaxResults());

		if (params.getSort() == SearchSort.DATE) {
			query.setSort(new Sort(new SortField("date", params.getSortType() == SearchSortType.DESC)));
		}
		else if (params.getSort() == SearchSort.RELEVANCE) {
			query.setSort(Sort.RELEVANCE);
		}

		return new SearchResult(query.list(), query.getResultSize());
	}

	private FullTextQuery createQuery(String criteria) throws ParseException {
		// FIXME: Should not hardcode the analyzer
		QueryParser parser = new QueryParser("text", new StandardAnalyzer());
		Query luceneQuery = parser.parse(criteria);

		return Search.createFullTextSession(this.sessionFactory.getCurrentSession())
			.createFullTextQuery(luceneQuery, Post.class);
	}
}
