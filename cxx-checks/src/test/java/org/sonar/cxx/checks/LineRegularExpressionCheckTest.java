/*
 * Sonar C++ Plugin (Community)
 * Copyright (C) 2011 Waleri Enns and CONTACT Software GmbH
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.cxx.checks;

import java.io.File;

import org.junit.Test;
import org.sonar.cxx.CxxAstScanner;
import org.sonar.squidbridge.api.SourceFile;
import org.sonar.squidbridge.checks.CheckMessagesVerifier;

public class LineRegularExpressionCheckTest {

  @Test
  public void lineRegExWithoutFilePattern() {
    LineRegularExpressionCheck check = new LineRegularExpressionCheck();
    check.regularExpression = "stdafx\\.h";
    check.message = "Found 'stdafx.h' in line!";

    SourceFile file = CxxAstScanner.scanSingleFile(new File("src/test/resources/checks/LineRegEx.cc"), check);
    CheckMessagesVerifier.verify(file.getCheckMessages())
      .next().atLine(2).withMessage(check.message)
      .next().atLine(3).withMessage(check.message)
      .noMore();
  }

  @Test
  public void lineRegExInvertWithoutFilePattern() {
    LineRegularExpressionCheck check = new LineRegularExpressionCheck();
    check.regularExpression = "//.*";
    check.invertRegularExpression = true;
    check.message = "Found no comment in the line!";

    SourceFile file = CxxAstScanner.scanSingleFile(new File("src/test/resources/checks/LineRegExInvert.cc"), check);
    CheckMessagesVerifier.verify(file.getCheckMessages())
      .next().atLine(3).withMessage(check.message)
      .noMore();
  }

  @Test
  public void lineRegExWithFilePattern1() {
    LineRegularExpressionCheck check = new LineRegularExpressionCheck();
    check.matchFilePattern = "/**/*.cc"; // all files with .cc file extension
    check.regularExpression = "#include\\s+\"stdafx\\.h\"";
    check.message = "Found '#include \"stdafx.h\"' in line in a .cc file!";

    SourceFile file = CxxAstScanner.scanSingleFile(new File("src/test/resources/checks/LineRegEx.cc"), check);
    CheckMessagesVerifier.verify(file.getCheckMessages())
      .next().atLine(2).withMessage(check.message)
      .next().atLine(3).withMessage(check.message)
      .noMore();
  }

  @Test
  public void lineRegExWithFilePatternInvert() {
    LineRegularExpressionCheck check = new LineRegularExpressionCheck();
    check.matchFilePattern = "/**/*.xx"; // all files with not .xx file extension
    check.invertFilePattern = true;
    check.regularExpression = "#include\\s+\"stdafx\\.h\"";
    check.message = "Found '#include \"stdafx.h\"' in line in a not .xx file!";

    SourceFile file = CxxAstScanner.scanSingleFile(new File("src/test/resources/checks/LineRegEx.cc"), check);
    CheckMessagesVerifier.verify(file.getCheckMessages())
      .next().atLine(2).withMessage(check.message)
      .next().atLine(3).withMessage(check.message)
      .noMore();
  }

  @Test
  public void lineRegExWithFilePattern2() {
    LineRegularExpressionCheck check = new LineRegularExpressionCheck();
    check.matchFilePattern = "/**/*.xx"; // all files with .xx file extension
    check.regularExpression = "#include\\s+\"stdafx\\.h\"";
    check.message = "Found '#include \"stdafx.h\"' in line in a .xx file!";

    SourceFile file = CxxAstScanner.scanSingleFile(new File("src/test/resources/checks/LineRegEx.cc"), check);
    CheckMessagesVerifier.verify(file.getCheckMessages())
      .noMore();
  }

}
