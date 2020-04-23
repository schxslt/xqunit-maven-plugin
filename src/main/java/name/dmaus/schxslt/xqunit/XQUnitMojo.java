/*
 * Copyright (C) 2020 by David Maus <dmaus@dmaus.name>
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package name.dmaus.schxslt.xqunit;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import org.basex.query.value.node.FElem;

import org.basex.query.func.unit.Suite;

import org.basex.core.jobs.Job;
import org.basex.core.Context;

import org.basex.io.IOFile;

import java.io.IOException;
import java.io.File;

@Mojo(name = "xqunit")
class XQUnitMojo extends AbstractMojo
{
    @Parameter(required = true)
    File path;

    public void execute () throws MojoExecutionException, MojoFailureException
    {
        Context ctx = new Context(false);
        Suite suite = new Suite();
        try {
            FElem result = suite.test(new IOFile(path), ctx, new Job() {});
            String message = String.format("[Total/Skipped/Failed/Errord] = [%d/%d/%d/%d]", suite.tests, suite.skipped, suite.failures, suite.errors);
            if (suite.failures > 0 || suite.errors > 0) {
                getLog().error(result.serialize().toString());
                getLog().error(message);
                throw new MojoFailureException("Some XQUnit tests failed");
            } else {
                getLog().info(message);
            }
        } catch (IOException e) {
            throw new MojoExecutionException("An error occurred while executing XQUnit", e);
        }
    }
}
