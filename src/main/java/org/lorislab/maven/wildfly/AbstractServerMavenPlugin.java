/*
 * Copyright 2014 Andrej Petras.
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
package org.lorislab.maven.wildfly;

import java.io.File;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * The abstract server MAVEN plug-in.
 *
 * @author Andrej Petras
 */
public abstract class AbstractServerMavenPlugin extends AbstractMojo {

    /**
     * The exploded flag.
     */
    @Parameter(defaultValue = "false", property = "org.lorislab.maven.wildfly.exploded")
    protected boolean exploded = false;
    
    /**
     * The Wildfly deployments absolute path directory.
     */    
    @Parameter(property = "org.lorislab.maven.wildfly.server.path")
    protected String absolutePath = null;
    
    /**
     * The Wildfly deployments directory.
     */
    @Parameter(defaultValue = "deployments", property = "org.lorislab.maven.wildfly.server.deployments")
    protected String deployments = "deployments";
    
    /**
     * The Wildfly profile.
     */
    @Parameter(defaultValue = "standalone", property = "org.lorislab.maven.wildfly.server.profile")
    protected String profile = "standalone";

    /**
     * The path of the file to deploy.
     */
    @Parameter(defaultValue = "${project.build.directory}/${project.build.finalName}.${project.packaging}", required = true)
    protected File deployFile;

    /**
     * The directory to deploy.
     */
    @Parameter(defaultValue = "${project.build.directory}/${project.build.finalName}", required = true)
    protected File deployDir;
        
    /**
     * The directory name in the deploy directory.
     */
    @Parameter(defaultValue = "${project.build.finalName}.${project.packaging}", required = true)
    protected String targetDirName;    
    
    /**
     * The Wildfly server directory.
     */
    @Parameter(property = "org.lorislab.maven.wildfly.server.dir")
    protected String widlflyDir;

    /**
     * The MAVEN project.
     */
    @Parameter( defaultValue = "${project}", readonly = true )
    protected MavenProject project;      

    /**
     * Gets the target directory.
     * 
     * @return the target directory.
     * 
     * @throws MojoExecutionException if the method fails.
     * @throws MojoFailureException if the method fails.
     */
    protected File getLocalTargetDir() throws MojoExecutionException, MojoFailureException {

        // check the deployment artifact
        if (deployFile == null || !deployFile.exists()) {
            throw new MojoFailureException("The build final name does not exists! Path: " + deployFile.getAbsolutePath());
        }
        
        // check the absolute path
        if (absolutePath != null && !absolutePath.isEmpty()) {
            File tmp = new File(absolutePath);
            if (!tmp.exists()) {
                throw new MojoFailureException("The path server deployment directory does not exists! Path: " + tmp.getAbsolutePath());
            }            
            return tmp;
        }
        
        if (widlflyDir == null || widlflyDir.isEmpty()) {
            throw new MojoFailureException("The Wildfly directory is not defined! property: org.lorislab.maven.wildfly.server.dir");
        }

        File tmp = new File(widlflyDir);
        if (!tmp.exists()) {
            throw new MojoFailureException("The Wildfly directory does not exists! Path: " + tmp.getAbsolutePath());
        }

        File profileDir = new File(widlflyDir, profile);
        File targetDir = new File(profileDir, deployments);

        if (!targetDir.exists()) {
            throw new MojoFailureException("The Wildfly deployment directory does not exists! Path: " + targetDir.getAbsolutePath());
        }

        return targetDir;
    }
  
}
