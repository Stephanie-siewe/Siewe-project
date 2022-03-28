import sys
import string
from hashlib import blake2b
from PyQt5 import QtGui
from PyQt5.QtGui import QGuiApplication
from PyQt5.QtWidgets import QApplication
from PyQt5.QtWidgets import QMainWindow
from Password_Generator import Ui_MainWindow

from random import randint, choice, sample

class MainWindow:
    def __init__(self):
        self.main_win = QMainWindow()
        self.ui = Ui_MainWindow()
        self.ui.setupUi(self.main_win)
        self.ui.stackedWidget.setCurrentWidget(self.ui.Home)
        self.ui.pushButton_Generate.clicked.connect(self.show_generate)
        self.ui.pushButton_old.clicked.connect(self.show_login)
        self.ui.continueButton_R.clicked.connect(self.save_register)
        self.ui.continueButton_L.clicked.connect(self.Verify_Password)
        self.ui.continueButton_L_3.clicked.connect(self.show_register)
        self.ui.Previous.clicked.connect(self.show_home)
        self.ui.Previous_2.clicked.connect(self.show_home)
        self.ui.Previous_L.clicked.connect(self.show_home)
        self.ui.Previous_R.clicked.connect(self.show_login)
        self.ui.Generate_button.clicked.connect(self.generate_password)
        self.ui.Copy_password.clicked.connect(self.Copy_password)
        self.ui.Letters_only.toggled.connect(self.onClicked)
        self.ui.Medium.toggled.connect(self.onClicked)
        self.ui.Strong.toggled.connect(self.onClicked)

        # adding a text from file into Listview

        self.models = QtGui.QStandardItemModel()
        self.ui.listView.setModel(self.models)
        with open("Password.txt", "r") as fic:
            print('okay')
            List_password = fic.readlines()
            List_password.reverse()
            print(List_password)
            for password in List_password:
                item = QtGui.QStandardItem(password)
                self.models.appendRow(item)
#active a hash fonction
        self.h = blake2b()

# the show functions allow you to view the different interfaces via the buttons
    def show(self):
        self.main_win.show()

    def show_register(self):
        self.ui.stackedWidget.setCurrentWidget(self.ui.Register)

    def show_login(self):
        self.ui.stackedWidget.setCurrentWidget(self.ui.Login)

    def show_home(self):
        self.ui.stackedWidget.setCurrentWidget(self.ui.Home)

    def show_generate(self):
        self.ui.stackedWidget.setCurrentWidget(self.ui.Generate)

    def show_old(self):
        self.ui.stackedWidget.setCurrentWidget(self.ui.ViewOld)


    def save_register(self):
        password = self.ui.password.text()
        print(password)
        self.h.update(bytes(password, "utf8"))
        print(self.h.hexdigest())

        with open("acces.txt","w+") as acc:
            acc.write(str(self.h.hexdigest()))
            print("save!")

        self.show_login()



    def Verify_Password(self):
        password = self.ui.password_2.text()
        print(password)
        with open("acces.txt", "r") as acc:
            p = acc.read()
            print(p)
            #active a another hash fonction
        self.h2 = blake2b()
        self.h2.update(bytes(password, "utf8"))
        b = self.h2.hexdigest()
        print(f"hashcode password {str(b)} and hashcode acces {p}")
        #error in condition lines 85 and 86
        print(type(password))
        print(type(p))

        if str(b) != p:
            print("error")
            self.ui.password_2.setText("")

        else:
            self.ui.password_2.setText("")
            self.show_old()


    def generate_password(self):
        self.password = ''
        if self.ui.Strong.isChecked():
            self.password = self.StrongPassword()
        elif self.ui.Medium.isChecked():
            self.password = self.MediumPassword()
        elif self.ui.Letters_only.isChecked():
            self.password = self.LettersOnlyPassword()
        else:
            pass
        self.ui.lineEdit_Password.setText(self.password)


    def Copy_password(self):
       cb = QGuiApplication.clipboard()
       # clear clipboard
       cb.clear(mode=cb.Clipboard)
       #copy clipboard
       cb.setText(self.ui.lineEdit_Password.text(), mode=cb.Clipboard)
      # save password in file
       with open("Password.txt", "a+") as fic:
           print('okay')
           fic.write(self.ui.lineEdit_Password.text()+'\n')




    def StrongPassword(self):
        password = ''
        for i in range(self.ui.spinBox.value()):
            p = chr(randint(33, 126))
            if self.ui.lineEdit_2_Avoid.text() == p:
                password += chr(randint(33, 126))
            else:
                password += p
        return password

    def MediumPassword(self):
        password = ''.join(sample(string.ascii_uppercase + string.ascii_lowercase + string.digits, self.ui.spinBox.value()))
        if self.ui.lineEdit_2_Avoid.text() in password:
            password.replace(self.ui.lineEdit_2_Avoid.text(), choice(string.ascii_letters))
        return password

    def LettersOnlyPassword(self):
        password = ''.join(sample(string.ascii_uppercase+string.ascii_lowercase, self.ui.spinBox.value()))
        if self.ui.lineEdit_2_Avoid.text() in password:
            password.replace(self.ui.lineEdit_2_Avoid.text(), choice(string.ascii_letters))

        return password

    def onClicked(self):
        pass





if __name__ == '__main__':
        app = QApplication(sys.argv)
        main_win = MainWindow()

        main_win.show()
        sys.exit(app.exec_())



