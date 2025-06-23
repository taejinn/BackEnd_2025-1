package com.example.bcsd.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bcsd.repository.ArticleRepository;
import com.example.bcsd.repository.BoardRepository;
import com.example.bcsd.repository.MemberRepository;
import com.example.bcsd.dto.ArticleRequestDto;
import com.example.bcsd.dto.ArticleResponseDto;
import com.example.bcsd.exception.InvalidRequestException;
import com.example.bcsd.exception.ResourceNotFoundException;
import com.example.bcsd.model.Article;
import com.example.bcsd.model.Board;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    public ArticleService(ArticleRepository articleRepository, MemberRepository memberRepository, BoardRepository boardRepository) {
        this.articleRepository = articleRepository;
        this.memberRepository = memberRepository;
        this.boardRepository = boardRepository;
    }

    public List<ArticleResponseDto> getAllArticles() {
        return articleRepository.findAll().stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public ArticleResponseDto getArticleByIdOrElseThrow(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 게시물입니다."));
        return convertToResponseDto(article);
    }
    
    public Article getArticleById(Long id) {
        return articleRepository.findById(id).orElse(null);
    }

    public List<ArticleResponseDto> getArticlesByMemberId(Long memberId) {
        return articleRepository.findByMemberId(memberId).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<ArticleResponseDto> getArticlesByBoardId(Long boardId) {
        return articleRepository.findByBoardId(boardId).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ArticleResponseDto createArticle(ArticleRequestDto articleDto) {
        validateMemberExists(articleDto.getMemberId());
        Board board = validateBoardExists(articleDto.getBoardId());
        
        Article article = new Article();
        article.setTitle(articleDto.getTitle());
        article.setContent(articleDto.getContent());
        article.setMemberId(articleDto.getMemberId());
        article.setBoard(board);
        
        Article savedArticle = articleRepository.save(article);
        return convertToResponseDto(savedArticle);
    }
    
    @Transactional
    public ArticleResponseDto updateArticle(Long id, ArticleRequestDto articleDto) {
        Article existingArticle = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 게시물입니다."));
        
        validateMemberExists(articleDto.getMemberId());
        Board board = validateBoardExists(articleDto.getBoardId());
        
        existingArticle.setTitle(articleDto.getTitle());
        existingArticle.setContent(articleDto.getContent());
        existingArticle.setMemberId(articleDto.getMemberId());
        existingArticle.setBoard(board);
        
        Article updatedArticle = articleRepository.save(existingArticle);
        return convertToResponseDto(updatedArticle);
    }

    @Transactional
    public void deleteArticleById(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 게시물입니다."));
        articleRepository.deleteById(id);
    }
    
    @Transactional
    public boolean deleteArticleIfExists(Long id) {
        if (articleRepository.findById(id).isEmpty()) {
            return false;
        }
        
        articleRepository.deleteById(id);
        return true;
    }

    private void validateMemberExists(Long memberId) {
        if (memberRepository.findById(memberId).isEmpty()) {
            throw new InvalidRequestException("존재하지 않는 사용자를 참조할 수 없습니다.");
        }
    }

    private Board validateBoardExists(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new InvalidRequestException("존재하지 않는 게시판을 참조할 수 없습니다."));
    }

    private ArticleResponseDto convertToResponseDto(Article article) {
        return new ArticleResponseDto(
                article.getId(),
                article.getMemberId(),
                article.getBoard() != null ? article.getBoard().getId() : null,
                article.getBoard() != null ? article.getBoard().getName() : null,
                article.getTitle(),
                article.getContent(),
                article.getCreatedAt(),
                article.getUpdatedAt()
        );
    }
} 