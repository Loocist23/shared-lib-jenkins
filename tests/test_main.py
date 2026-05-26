#!/usr/bin/env python3
"""
Tests unitaires pour le module main.py
Utilise la bibliothèque unittest de Python
"""

import unittest
from src.main import calculer_prix_ttc, appliquer_reduction, calculer_prix_final


class TestCalculsEcommerce(unittest.TestCase):
    """Classe de tests pour les fonctions de calcul e-commerce"""

    # ========== Tests pour calculer_prix_ttc ==========
    
    def test_calculer_prix_ttc_standard(self):
        """Test du calcul TTC avec TVA standard (20%)"""
        self.assertEqual(calculer_prix_ttc(100.0), 120.0)
        self.assertEqual(calculer_prix_ttc(50.0), 60.0)
        self.assertEqual(calculer_prix_ttc(0.0), 0.0)

    def test_calculer_prix_ttc_tva_custom(self):
        """Test du calcul TTC avec TVA personnalisée"""
        self.assertEqual(calculer_prix_ttc(100.0, 0.1), 110.0)  # 10% TVA
        self.assertEqual(calculer_prix_ttc(100.0, 0.05), 105.0)  # 5% TVA
        self.assertEqual(calculer_prix_ttc(100.0, 0.0), 100.0)  # 0% TVA

    def test_calculer_prix_ttc_prix_negatif(self):
        """Test qu'une erreur est levée pour un prix négatif"""
        with self.assertRaises(ValueError):
            calculer_prix_ttc(-100.0)

    def test_calculer_prix_ttc_tva_negative(self):
        """Test qu'une erreur est levée pour une TVA négative"""
        with self.assertRaises(ValueError):
            calculer_prix_ttc(100.0, -0.2)

    # ========== Tests pour appliquer_reduction ==========

    def test_appliquer_reduction_standard(self):
        """Test de l'application d'une réduction standard"""
        self.assertEqual(appliquer_reduction(100.0, 0.1), 90.0)  # 10% de réduction
        self.assertEqual(appliquer_reduction(100.0, 0.25), 75.0)  # 25% de réduction
        self.assertEqual(appliquer_reduction(100.0, 0.0), 100.0)  # 0% de réduction

    def test_appliquer_reduction_prix_zero(self):
        """Test avec un prix de 0"""
        self.assertEqual(appliquer_reduction(0.0, 0.5), 0.0)

    def test_appliquer_reduction_prix_negatif(self):
        """Test qu'une erreur est levée pour un prix négatif"""
        with self.assertRaises(ValueError):
            appliquer_reduction(-100.0, 0.1)

    def test_appliquer_reduction_invalide_negative(self):
        """Test qu'une erreur est levée pour une réduction négative"""
        with self.assertRaises(ValueError):
            appliquer_reduction(100.0, -0.1)

    def test_appliquer_reduction_invalide_sup_100(self):
        """Test qu'une erreur est levée pour une réduction > 100%"""
        with self.assertRaises(ValueError):
            appliquer_reduction(100.0, 1.5)

    # ========== Tests pour calculer_prix_final ==========

    def test_calculer_prix_final_complet(self):
        """Test du calcul complet avec TVA et réduction"""
        # Prix HT = 100, TVA 20% = 120, puis -10% = 108
        self.assertEqual(calculer_prix_final(100.0, 0.2, 0.1), 108.0)
        
        # Prix HT = 50, TVA 10% = 55, puis -20% = 44
        self.assertEqual(calculer_prix_final(50.0, 0.1, 0.2), 44.0)

    def test_calculer_prix_final_sans_reduction(self):
        """Test du calcul final sans réduction"""
        self.assertEqual(calculer_prix_final(100.0, 0.2, 0), 120.0)

    def test_calculer_prix_final_sans_tva(self):
        """Test du calcul final sans TVA"""
        self.assertEqual(calculer_prix_final(100.0, 0.0, 0.1), 90.0)

    def test_calculer_prix_final_prix_zero(self):
        """Test avec un prix de 0"""
        self.assertEqual(calculer_prix_final(0.0, 0.2, 0.1), 0.0)


if __name__ == '__main__':
    unittest.main()
