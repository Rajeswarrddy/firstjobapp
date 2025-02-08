package com.embarkx.firstjobapp.review.impl;

import com.embarkx.firstjobapp.company.Company;
import com.embarkx.firstjobapp.company.CompanyService;
import com.embarkx.firstjobapp.review.Review;
import com.embarkx.firstjobapp.review.ReviewRepository;
import com.embarkx.firstjobapp.review.ReviewService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final CompanyService companyService;

    public ReviewServiceImpl(ReviewRepository reviewRepository,CompanyService  companyService) {
        this.reviewRepository = reviewRepository;
        this.companyService =companyService;
    }

    @Override
    public List<Review> getAllReviews(Long companyId) {
         List<Review> reviews = reviewRepository.findByCompanyId(companyId);
        return reviews;
    }

    @Override
    public boolean addReview(Long companyId, Review review){
      Company company = companyService.getCompanyById(companyId);
      if(company !=null) {
          review.setCompany(company);
          reviewRepository.save(review);
          return true;
      }
      else{
          return false;
      }
    }

    @Override
    public Review getReview(Long companyId, Long reviewId) {
        List<Review> reviews =reviewRepository.findByCompanyId(companyId);
        return reviews.stream()
                .filter(review -> review.getId().equals(reviewId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean updateReview(Long companyId, Long reviewId, Review updatedReview) {
      if(companyService.getCompanyById(companyId) != null){
          updatedReview.setCompany(companyService.getCompanyById(companyId));
          updatedReview.setId(reviewId);
          reviewRepository.save(updatedReview);
          return true;
      }else{
          return false;
      }
    }

    @Override
    public boolean deleteReview(Long companyId, Long reviewId) {
        // Check if the company exists and the review exists
        if (companyService.getCompanyById(companyId) != null && reviewRepository.existsById(reviewId)) {

            // Find the review
            Review review = reviewRepository.findById(reviewId).orElse(null);

            // If the review is found ,and it is associated with the correct company
            if (review != null && review.getCompany().getId().equals(companyId)) {
                Company company = review.getCompany();
                // Remove the review from the company's list of reviews
                company.getReviews().remove(review);

                // Update the company with the modified list of reviews
                companyService.updateCompany(company, companyId);

                // Delete the review from the repository
                reviewRepository.deleteById(reviewId);

                return true;
            }
        }

        return false;

    }
}
