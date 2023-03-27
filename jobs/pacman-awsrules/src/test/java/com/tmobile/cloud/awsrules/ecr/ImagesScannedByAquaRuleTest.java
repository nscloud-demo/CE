/*******************************************************************************
 * Copyright 2018 T Mobile, Inc. or its affiliates. All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.tmobile.cloud.awsrules.ecr;

import com.tmobile.cloud.awsrules.ec2.ResourceScannedByQualysRule;
import com.tmobile.cloud.awsrules.utils.CommonTestUtils;
import com.tmobile.cloud.awsrules.utils.PacmanUtils;
import com.tmobile.pacman.commons.exception.InvalidInputException;
import com.tmobile.pacman.commons.exception.RuleExecutionFailedExeption;
import com.tmobile.pacman.commons.policy.Annotation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ PacmanUtils.class,Annotation.class})
public class ImagesScannedByAquaRuleTest {

    @InjectMocks
    ImagesScannedByAquaRule imagesScannedByAquaRule;
 
    @Test
    public void executeTest() throws Exception {
        mockStatic(PacmanUtils.class);
        mockStatic(Annotation.class);
        when(PacmanUtils.doesAllHaveValue(anyString(),anyString(),anyString(),anyString())).thenReturn(
                true);
        when(PacmanUtils.formatUrl(anyObject(),anyString())).thenReturn("host");
        when(PacmanUtils.calculateLaunchedDuration(anyObject())).thenReturn(10l);

        when(PacmanUtils.checkImageIdFromElasticSearchForAqua(anyString(),anyString(),anyString(),anyString())).thenReturn(CommonTestUtils.getListObject("123"));
        assertThat(imagesScannedByAquaRule.execute(CommonTestUtils.getMapString("r_123 "),CommonTestUtils.getMapString("r_123 ")), is(nullValue()));

        when(PacmanUtils.checkImageIdFromElasticSearchForAqua(anyString(),anyString(),anyString(),anyString())).thenReturn(CommonTestUtils.getEmptyList());
        when(Annotation.buildAnnotation(anyMap(), any())).thenReturn(new Annotation());
        assertThat(imagesScannedByAquaRule.execute(CommonTestUtils.getMapString("r_123 "),CommonTestUtils.getMapString("r_123 ")), is(notNullValue()));

        when(PacmanUtils.calculateDuration(anyObject())).thenReturn(40l);
        when(PacmanUtils.checkImageIdFromElasticSearchForAqua(anyString(),anyString(),anyString(),anyString())).thenReturn(CommonTestUtils.getListObjectWithExpiredScan("123"));
        assertThat(imagesScannedByAquaRule.execute(CommonTestUtils.getMapString("r_123 "),CommonTestUtils.getMapString("r_123 ")), is(notNullValue()));

    }
    
    @Test
    public void getHelpTextTest(){
        assertThat(imagesScannedByAquaRule.getHelpText(), is(nullValue()));
    }
}