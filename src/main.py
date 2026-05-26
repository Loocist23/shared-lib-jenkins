#!/usr/bin/env python3
"""
Module principal de l'application e-commerce
Contient des fonctions de calcul de base pour la démonstration
"""


def calculer_prix_ttc(prix_ht: float, tva: float = 0.2) -> float:
    """
    Calcule le prix TTC à partir du prix HT et du taux de TVA
    
    Args:
        prix_ht: Prix hors taxes
        tva: Taux de TVA (par défaut 0.2 pour 20%)
    
    Returns:
        Prix toutes taxes comprises
    """
    if prix_ht < 0:
        raise ValueError("Le prix HT ne peut pas être négatif")
    if tva < 0:
        raise ValueError("Le taux de TVA ne peut pas être négatif")
    return prix_ht * (1 + tva)


def appliquer_reduction(prix: float, reduction: float) -> float:
    """
    Applique une réduction en pourcentage sur un prix
    
    Args:
        prix: Prix initial
        reduction: Pourcentage de réduction (ex: 0.1 pour 10%)
    
    Returns:
        Prix après réduction
    """
    if prix < 0:
        raise ValueError("Le prix ne peut pas être négatif")
    if reduction < 0 or reduction > 1:
        raise ValueError("La réduction doit être entre 0 et 1")
    return prix * (1 - reduction)


def calculer_prix_final(prix_ht: float, tva: float = 0.2, reduction: float = 0) -> float:
    """
    Calcule le prix final après application de la TVA et des réductions
    
    Args:
        prix_ht: Prix hors taxes
        tva: Taux de TVA
        reduction: Pourcentage de réduction
    
    Returns:
        Prix final TTC après réduction
    """
    prix_ttc = calculer_prix_ttc(prix_ht, tva)
    return appliquer_reduction(prix_ttc, reduction)


if __name__ == "__main__":
    # Exemple d'utilisation
    prix = 100.0
    print(f"Prix HT: {prix}€")
    print(f"Prix TTC (20% TVA): {calculer_prix_ttc(prix)}€")
    print(f"Prix final avec 10% de réduction: {calculer_prix_final(prix, reduction=0.1)}€")
