/*
 * Copyright 2013 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.netflix.glisten

import java.lang.reflect.Method

/**
 * Wraps the execution of a workflow.
 */
class LocalWorkflowExecuter {

    private final Object workflow
    private final LocalWorkflowOperations workflowOperations

    static <T> T makeLocalWorkflowExecuter(T workflow, LocalWorkflowOperations workflowOperations) {
        new LocalWorkflowExecuter(workflow, workflowOperations)
    }

    private LocalWorkflowExecuter(Object workflow, LocalWorkflowOperations workflowOperations) {
        this.workflow = workflow
        this.workflowOperations = workflowOperations
    }

    def methodMissing(String name, args) {
        ReflectionHelper reflectionHelper = new ReflectionHelper(workflow.getClass())
        Method method = reflectionHelper.findMethodForNameAndArgsOrFail(name, args as List)
        method.invoke(workflow, args as Object[])
        workflowOperations.workflowExecutionComplete()
    }
}