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
package net.jforum.repository;

import java.util.List;

import net.jforum.entities.Category;
import net.jforum.entities.Forum;

/**
 * @author Rafael Steil
 */
public interface CategoryRepository extends Repository<Category> {

	/**
	 * Get all categories data from the database.
	 *
	 * @return all categories found
	 */
	public List<Category> getAllCategories();

	/**
	 * @param category
	 * @return
	 */
	public List<Forum> getForums(Category category);
}