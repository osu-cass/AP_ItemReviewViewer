package org.smarterbalanced.itemreviewviewer.web.mocks;

import org.apache.commons.lang.StringEscapeUtils;
import org.smarterbalanced.itemreviewviewer.web.models.*;

import java.util.ArrayList;
import java.util.List;

public class MockItemScoringModel {
    public ItemScoringModel itemScoringModel;

    public MockItemScoringModel(){
        List<RubricModel> rubrics = new ArrayList<RubricModel>();
        List<RubricEntryModel> rubricEntries = new ArrayList<RubricEntryModel>();
        List<RubricSampleModel> rubricSamples = new ArrayList<RubricSampleModel>();

        rubricEntries.add(new RubricEntryModel("2", "\n        Rubric 2", "<p style=\"\">A response:</p><p style=\"\">• Gives sufficient evidence of the ability to determine/summarize the theme/central idea/message, or to summarize what happens after or during a key event</p><p style=\"\">• Includes specific examples/details that make clear reference to the text</p><p style=\"\">• Adequately explains the theme/central idea/message or summary with clearly relevant information based on the text</p>');"));
        rubricEntries.add(new RubricEntryModel("1","\n        Rubric 1", "<p style=\"\">A response:</p><p style=\"\">• Gives limited evidence of the ability to determine/summarize the theme/central idea/message, or to summarize what happens after or during a key event</p><p style=\"\">• Includes vague/limited examples/details that make reference to the text</p><p style=\"\">• Explains the theme/central idea/message or summary with vague/limited information based on the text</p>"));
        rubricEntries.add(new RubricEntryModel("0","\n        Rubric 0)", "<p style=\"\">A response:</p><p style=\"\">• Gives no evidence of the ability to determine/summarize the theme/central idea/message, or to summarize what happens after or during a key event</p><p style=\"\">OR</p><p style=\"\">• Gives the theme/central idea/message or summary, but includes no examples or no examples/details that make reference to the text</p><p style=\"\">OR</p><p style=\"\">• Gives the theme/central idea/message or summary, but includes no explanation or no relevant information from the text</p>"));
        rubrics.add(new RubricModel("English",rubricEntries,rubricSamples));
        rubrics.add(new RubricModel("Spanish",rubricEntries,rubricSamples));

        List<ItemScoringOptionModel> scoreOptions = new ArrayList<ItemScoringOptionModel>();
        scoreOptions.add(new ItemScoringOptionModel("","","","",""));
        scoreOptions.add(new ItemScoringOptionModel("","","","",""));
        scoreOptions.add(new ItemScoringOptionModel("","","","",""));

        this.itemScoringModel = new ItemScoringModel("",true, scoreOptions,rubrics);
    }

}
