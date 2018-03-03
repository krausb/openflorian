package de.openflorian.alarm.alert.impl;

/*
 * This file is part of Openflorian.
 *
 * Copyright (C) 2015 - 2018  Bastian Kraus
 *
 * Openflorian is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version)
 *
 * Openflorian is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Openflorian.  If not, see <http://www.gnu.org/licenses/>.
 */

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import de.openflorian.OperationGenerator;
import de.openflorian.config.OpenflorianConfig;
import de.openflorian.crypt.CryptCipherService;
import de.openflorian.data.model.Operation;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

/**
 * URL Alerter Verticle Unit Test
 */
public class UrlAlerterVerticleTest {

    private static final Logger log = LoggerFactory.getLogger(UrlAlerterVerticleTest.class);

    private static final List<OpenflorianConfig.Alerter> alerters = OpenflorianConfig.config().alerter;

    @ClassRule
    public static final WireMockRule httpServerMock = new WireMockRule();

    @Test
    public void testUrlAlertNotEncrypted() throws Exception {

        OpenflorianConfig.Alerter alerter = alerters.get(0);
        alerter.encryptPayload = false;
        String url = String.format("%s://%s:%d%s", alerter.protocol, alerter.host, alerter.port, alerter.path);

        Operation probe = OperationGenerator.generate();
        String probeJson = OperationGenerator.generateJson(probe);

        StubMapping requestStub = httpServerMock.stubFor(
                post(urlPathEqualTo(alerter.path))
                        .withRequestBody(equalToJson(probeJson))
                .willReturn(ok())
        );

        log.info("Sending probe: " + probeJson);
        UrlAlerterVerticle urlAlerter = new UrlAlerterVerticle(alerter);
        urlAlerter.alert(probe);

        verify(exactly(1),
                postRequestedFor(urlPathEqualTo(alerter.path))
                        .withRequestBody(equalToJson(probeJson))
        );

    }

    @Test
    public void testUrlAlertEncrypted() throws Exception {

        OpenflorianConfig.Alerter alerter = alerters.get(0);
        String url = String.format("%s://%s:%d%s", alerter.protocol, alerter.host, alerter.port, alerter.path);

        Operation probe = OperationGenerator.generate();
        String probeJson = OperationGenerator.generateJson(probe);

        if(alerter.encryptPayload) {
            probeJson = CryptCipherService.service().encrypt(
                    CryptCipherService.service().encrypt(
                            probeJson,
                            CryptCipherService.CipherTarget.Blowfish
                    ),
                    CryptCipherService.CipherTarget.Xor
            );
        }

        StubMapping requestStub = httpServerMock.stubFor(
                post(urlPathEqualTo(alerter.path))
                        .withRequestBody(equalTo(probeJson))
                        .willReturn(ok())
        );

        log.info("Sending probe: " + probeJson);
        UrlAlerterVerticle urlAlerter = new UrlAlerterVerticle(alerter);
        urlAlerter.alert(probe);

        verify(exactly(1),
                postRequestedFor(urlPathEqualTo(alerter.path))
                        .withRequestBody(equalTo(probeJson))
        );

    }

}
