from django.test import TestCase

class ManagerAccountTest(TestCase):
    def setUpTestData(cls):
        print("setUpTestData: Run once to set up non-modified data for all class methods.")
        pass

    def setUp(self):
        self.client = Client()
        print("setUp: Run once for every test method to setup clean data.")
        pass

    def test_manageraccount(self):
        url = reverse('clubManagement')
        response = self.client.get(url)
        self.assertEqual(response.status_code, 200)
        self.assertTemplateUsed(response, 'clubManagement.html')
        self.assertContains(response, 'clubID')

class ClubTest(TestCase):
    @classmethod
    def setUpTestData(cls):
        print("setUpTestData: Run once to set up non-modified data for all class methods.")
        pass

    def setUp(self):
        self.client = Client()
        print("setUp: Run once for every test method to setup clean data.")
        pass

    def test_addclub(self):
        url = reverse('clubManagement')
        response = self.client.get(url)
        self.assertEqual(response.status_code, 200)
        self.assertTemplateUsed(response, 'clubManagement.html')
        self.assertContains(response, 'clubID')

    def test_deleteclub(self):
        url = reverse('clubManagement')
        response = self.client.get(url)
        self.assertEqual(response.status_code, 200)
        self.assertTemplateUsed(response, 'clubManagement.html')
        self.assertContains(response, 'clubID')