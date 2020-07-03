from django.test import TestCase

class LoginTest(TestCase):
    @classmethod
    def setUpTestData(cls):
        print("setUpTestData: Run once to set up non-modified data for all class methods.")
        pass

    def setUp(self):
        self.client = Client()
        print("setUp: Run once for every test method to setup clean data.")
        pass

    def test_Login(self):
        url = reverse('adminLogin')
        response = self.client.get(url)
        self.assertEqual(response.status_code, 200)
        self.assertTemplateUsed(response, 'adminLogin.html')
        #self.assertContains(response, 'adsID')