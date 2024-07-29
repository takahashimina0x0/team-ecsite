package jp.co.internous.team2405.controller;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.internous.team2405.model.domain.MstCategory;
import jp.co.internous.team2405.model.domain.MstProduct;
import jp.co.internous.team2405.model.form.SearchForm;
import jp.co.internous.team2405.model.mapper.MstCategoryMapper;
import jp.co.internous.team2405.model.mapper.MstProductMapper;
import jp.co.internous.team2405.model.session.LoginSession;

/**
 * 商品検索に関する処理を行うコントローラー
 * @author インターノウス
 *
 */
@Controller
@RequestMapping("/team2405")
public class IndexController {
	
	/*
	 * フィールド定義
	 */
	@Autowired
	private MstCategoryMapper categoryMapper;
	
	@Autowired
	private MstProductMapper productMapper;
	
	@Autowired
	private LoginSession loginSession;
	
	/**
	 * トップページを初期表示する。
	 * @param m 画面表示用オブジェクト
	 * @return トップページ
	 */
	@RequestMapping("/")
	public String index(Model m) {
		
		List<MstCategory> categories = categoryMapper.findAll();
		m.addAttribute("categories", categories);
		
		List<MstProduct> products = productMapper.findAll();
		m.addAttribute("products", products);
		
		boolean isLoggedIn = loginSession.getLogined();
		int tmpUserId = loginSession.getTmpUserId();
		
		if (isLoggedIn == false && tmpUserId == 0) {
			Random rand = new Random();
			int value = -1 * (100000000 + rand.nextInt(900000000));
			loginSession.setTmpUserId(value);
		}
		m.addAttribute("loginSession", loginSession);
		
		return "index";
		
	}
	
	/**
	 * 検索処理を行う
	 * @param f 検索用フォーム
	 * @param m 画面表示用オブジェクト
	 * @return トップページ
	 */
	@RequestMapping("/searchItem")
	public String searchItem(SearchForm f, Model m) {
		
		int category = f.getCategory();
		String[] keywords;
		
		String editWords = f.getKeywords().replaceAll("　", " ").replaceAll(" {2,}", " ").trim();
		keywords = editWords.split(" ");
		
		List<MstProduct> products  = null;
		if (keywords.length == 0 && category == 0) {
			products = productMapper.findAll();
		} else if (category == 0) {
			products = productMapper.findByProductName(keywords);
		} else {
			products = productMapper.findByCategoryAndProductName(category, keywords);
		}
		
		List<MstCategory> categories = categoryMapper.findAll();
		m.addAttribute("categories", categories);
		m.addAttribute("products", products);
		m.addAttribute("selected", category);
		m.addAttribute("keywords", editWords);
		m.addAttribute("loginSession", loginSession);
		
		return "index";
		
	}
}
