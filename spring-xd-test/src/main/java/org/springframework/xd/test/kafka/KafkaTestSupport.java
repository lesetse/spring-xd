/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.xd.test.kafka;


import kafka.utils.ZKStringSerializer$;
import kafka.utils.ZkUtils;
import org.I0Itec.zkclient.ZkClient;
import org.junit.Rule;

import org.springframework.xd.test.AbstractExternalResourceTestSupport;

/**
 * JUnit {@link Rule} that detects if Kafka is available on localhost.
 * 
 * @author Ilayaperumal Gopinathan
 * @since 1.1
 */
public class KafkaTestSupport extends AbstractExternalResourceTestSupport<String> {

	private final String zkConnectString;

	private ZkClient zkClient;

	public KafkaTestSupport(String zkConnectString) {
		super("KAFKA");
		this.zkConnectString = zkConnectString;
	}

	@Override
	protected void obtainResource() throws Exception {
		try {
			this.zkClient = new ZkClient(zkConnectString, 5000, 5000, ZKStringSerializer$.MODULE$);
			if (ZkUtils.getAllBrokersInCluster(zkClient).size() == 0) {
				throw new RuntimeException("Kafka server not available on localhost");
			}
		}
		catch (Exception e) {
			throw new RuntimeException("Issues connecting to ZK server");
		}
	}

	@Override
	protected void cleanupResource() throws Exception {
		// do nothing
	}

	public ZkClient getZkClient() {
		return this.zkClient;
	}

}