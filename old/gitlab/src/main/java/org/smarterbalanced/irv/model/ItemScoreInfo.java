/**
 * 
 */
package org.smarterbalanced.irv.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import tds.itemscoringengine.ScoreRationale;
import tds.itemscoringengine.ScoringStatus;

/**
 * @author kthotti
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemScoreInfo {

	@JsonProperty("configLevel")
	private String confLevel;

	@JsonProperty("points")
	private int points;

	@JsonProperty("dimension")
	private String dimension;

	@JsonProperty("status")
	private ScoringStatus status;

	@JsonProperty("rationale")
	private ScoreRationale rationale;

	@JsonProperty("subScore")
	private List<ItemScoreInfo> _subScores = new ArrayList<ItemScoreInfo>();

	@JsonProperty("maxScore")
	private int maxScore;

	public ItemScoreInfo(int points, int maxPoints, ScoringStatus status, String dimension, ScoreRationale rationale) {
		this.points = points;
		this.maxScore = maxPoints;
		this.status = status;
		this.dimension = dimension;
		this.rationale = rationale;
	}

	public String getConfLevel() {
		return confLevel;
	}

	public void setConfLevel(String confLevel) {
		this.confLevel = confLevel;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public String getDimension() {
		return dimension;
	}

	public void setDimension(String dimension) {
		this.dimension = dimension;
	}

	public ScoringStatus getStatus() {
		return status;
	}

	public void setStatus(ScoringStatus status) {
		this.status = status;
	}

	public ScoreRationale getRationale() {
		return rationale;
	}

	public void setRationale(ScoreRationale rationale) {
		this.rationale = rationale;
	}

	public List<ItemScoreInfo> get_subScores() {
		return _subScores;
	}

	public void set_subScores(List<ItemScoreInfo> _subScores) {
		this._subScores = _subScores;
	}

	public int getMaxScore() {
		return maxScore;
	}

	public void setMaxScore(int maxScore) {
		this.maxScore = maxScore;
	}

}
