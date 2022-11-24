package vn.co.honda.hondacrm.interactors.newsDetailed;

import vn.co.honda.hondacrm.interactors.common.BaseInteractor;
import vn.co.honda.hondacrm.repositories.newsDetailed.INewsDetailedFragmentRepository;

/**
 * @author CuongNV31
 */
public class NewsDetailedFragmentInteractor extends BaseInteractor implements INewsDetailedFragmentInteractor {

    private final INewsDetailedFragmentRepository mNewsDetailedFragmentRepository;

    public NewsDetailedFragmentInteractor(INewsDetailedFragmentRepository newsDetailedFragmentRepository) {
        this.mNewsDetailedFragmentRepository = newsDetailedFragmentRepository;
    }
}
