package uz.najottalim.javan6.repository.extension.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uz.najottalim.javan6.entity.Product;
import uz.najottalim.javan6.repository.extension.ProductExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductExtensionImpl implements ProductExtension {
    private final EntityManager entityManager;


    @Override
    public List<Product> searchByNameAndCategoryAndPrice(Optional<List<String>> names, Optional<List<String>> category, Optional<Double> minValue, Optional<Double> maxValue) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Product> query = cb.createQuery(Product.class);
        Root<Product> root = query.from(Product.class);

        List<Predicate> predicates = new ArrayList<>();

        names.ifPresent(name -> {
                    List<Predicate> namePredicates = new ArrayList<>();

                    name.forEach(n -> {
                        namePredicates.add(cb.like(root.get("name"), "%" + n + "%"));
                    });
                    Predicate[] predicatesN = new Predicate[namePredicates.size()];
                    for (int i = 0; i < predicatesN.length; i++) {
                        predicatesN[i] = namePredicates.get(i);
                    }

                    predicates.add(cb.or(predicatesN));
                }
        );

        category.ifPresent(categories -> {
            List<Predicate> categoryPredicates = new ArrayList<>();
            categories.forEach(cat -> {
                categoryPredicates.add(cb.like(root.get("category"), "%" + cat + "%"));
            });
            Predicate[] predicatesCat = new Predicate[categoryPredicates.size()];
            for (int i = 0; i < predicatesCat.length; i++) {
                predicatesCat[i] = categoryPredicates.get(i);
            }
            predicates.add(cb.or(predicatesCat));
        });

        minValue.ifPresent(minVal -> predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minVal)));
        maxValue.ifPresent(maxVal -> predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxVal)));

        Predicate[] predicatesArray = new Predicate[predicates.size()];
        for (int i = 0; i < predicates.size(); i++) {
            predicatesArray[i] = predicates.get(i);
        }

        query.select(root).where(cb.and(predicatesArray));
        return entityManager.createQuery(query).getResultList();

    }
}












