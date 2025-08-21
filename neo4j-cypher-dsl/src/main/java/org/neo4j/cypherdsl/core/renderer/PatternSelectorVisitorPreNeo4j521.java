/*
 * Copyright (c) 2019-2025 "Neo4j,"
 * Neo4j Sweden AB [https://neo4j.com]
 *
 * This file is part of Neo4j.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.neo4j.cypherdsl.core.renderer;

import java.util.ArrayDeque;
import java.util.Deque;

import org.neo4j.cypherdsl.core.PatternSelector;
import org.neo4j.cypherdsl.core.ast.Visitable;
import org.neo4j.cypherdsl.core.ast.Visitor;

final class PatternSelectorVisitorPreNeo4j521 implements Visitor {

	private final DefaultVisitor delegate;

	private final Deque<Boolean> handled = new ArrayDeque<>();

	PatternSelectorVisitorPreNeo4j521(DefaultVisitor delegate) {
		this.delegate = delegate;
	}

	@Override
	public void enter(Visitable segment) {
		if (segment instanceof PatternSelector.ShortestK shortestK && shortestK.getK() == 1) {
			this.delegate.builder.append("shortestPath(");
			this.handled.push(true);
		}
		else if (segment instanceof PatternSelector.AllShortest) {
			this.delegate.builder.append("allShortestPaths(");
			this.handled.push(true);
		}
		else {
			this.delegate.enter(((PatternSelector) segment));
			this.handled.push(false);
		}
	}

	@Override
	public void leave(Visitable segment) {
		if (this.handled.pop()) {
			this.delegate.builder.append(")");
		}
		else {
			this.delegate.leave(segment);
		}
	}

}
