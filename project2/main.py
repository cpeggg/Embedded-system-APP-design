import sys
from PyQt5.QtCore import *
from PyQt5.QtGui import *
from PyQt5.QtWidgets import *
from PyQt5.uic import loadUi

# class hello(QDialog):
# 	def __init__(self):
# 		super(hello,self).__init__()
# 		loadUi('test.ui',self)
# 		self.setWindowTitle('hhhhh')
# 		self.pushButton.clicked.connect(self.on_pushButton_clicked)
# 	@pyqtSlot()
# 	def on_pushButton_clicked(self):
# 		self.label.setText('Welcome :'+self.lineEdit.text())


class mytest(QWidget):
	"""docstring for mytest"""
	def __init__(self):
		super(mytest, self).__init__()
		# self.arg = arg
		loadUi('threat_analysis.ui',self)
		self.setWindowTitle('test')
		mainlayout=layout = QGridLayout(self.tab6)
		self.area = PaintArea()
		mainlayout.addWidget(self.area) 
		self.tab6.setLayout(mainlayout)
		
		self.login.clicked.connect(self.login_clk)
		self.register_2.clicked.connect(self.register_clk)
		self.search.clicked.connect(self.search_clk)
		# self.login.clicked.connect(self.login_clk)
		# self.login.clicked.connect(self.login_clk)

		self.lg=Login()
		self.rg=Register()
	# @pyqtSlot()
	def login_clk(self):
		# self.label.setText('Welcome :'+self.lineEdit.text())
		self.lg.exec()
		user=self.lg.getuser()
		# self.setuser(user)#需要实现
		#更新当前用户信息
	def register_clk(self):
		self.rg.exec()

	def search_clk(self):
		if(self.lineEdit.text()==""):
			return
		if(self.lineEdit.text().split(".")[-1].isnumeric()):
			#ip
			QMessageBox.about(self, "Warning", "ip")
		else:
			#domin name
			QMessageBox.about(self, "Warning", "domin name")


class Login(QDialog):
	def __init__(self):
		super(Login,self).__init__()
		loadUi('Login.ui',self)
		self.setWindowTitle('Log in')
		self.submit.clicked.connect(self.on_pushButton_clicked)
		self.user=''
	@pyqtSlot()
	def on_pushButton_clicked(self):
		usrtmp=self.lineEdit.text()
		pwd=self.lineEdit_2.text()
		if(self.match(usr=usrtmp,pwd=pwd)):
			self.user=usrtmp
			self.close()
		else:
			QMessageBox.about(self, "Warning", "PASSWORD WRONG!!!")

	def match(self,usr,pwd):
		return False

	def getuser(self):
		return self.user


		# self.label.setText('Welcome :'+self.lineEdit.text())

class Register(QDialog):
	def __init__(self):
		super(Register,self).__init__()
		loadUi('Register.ui',self)
		self.setWindowTitle('Register')
		self.pushButton.clicked.connect(self.on_pushButton_clicked)
	@pyqtSlot()
	def on_pushButton_clicked(self):
		usrtmp=self.lineEdit.text()
		pwd=self.lineEdit_2.text()
		if(~self.check_dup(usrtmp)):
			self.insert(usrtmp,pwd)
		else:
			QMessageBox.about(self, "Warning", "USER EXISTED!!!")
		# self.label.setText('Welcome :'+self.lineEdit.text())
	def check_dup(self,usr):
		return False
	def insert(self,usr,pwd):
		return null

class PaintArea(QWidget):
    def __init__(self):
        super(PaintArea,self).__init__()
        self.Shape = ["Line","Rectangle", 'Rounded Rectangle', "Ellipse", "Pie", 'Chord', 
    "Path","Polygon", "Polyline", "Arc", "Points", "Text", "Pixmap"]
        self.setPalette(QPalette(Qt.white))
        self.setAutoFillBackground(True)
        self.setMinimumSize(1000,1000)
        self.pen = QPen()
        self.brush = QBrush()        
    
    def setShape(self,s):
        self.shape = s
        self.update()
        
    def setPen(self,p):
        self.pen = p
        self.update()
    
    def setBrush(self,b):
        self.brush = b
        self.update()
    
    def paintEvent(self,QPaintEvent):
        p = QPainter(self)
        p.setPen(self.pen)
        p.setBrush(self.brush)
        
        rect = QRect(0,100,100,200) 
        points = [QPoint(150,100),QPoint(300,150),QPoint(350,250),QPoint(100,300)]
        startAngle = 30 * 16
        spanAngle = 120 * 16
        
        path = QPainterPath();
        path.addRect(150,150,100,100)
        path.moveTo(100,100)
        path.cubicTo(300,100,200,200,300,300)
        path.cubicTo(100,300,200,200,100,100)
        ########################## paint relationship #########################################
        #######################################################################################
        rpoints=['55.10.162.0','0.0.0.0','100.123.45.6','123.4.5.6']
        rlen=len(rpoints)
        r=5*10
        dis=15*10
        hight=40*10
        assert dis>=2*r+1
        centers=[]
        rects=[]
        for i in range(len(rpoints)) :
            centers.append((i+1)*dis)   
            rects.append(QRect(centers[i]-r,hight-r,2*r,2*r))

        for rr in rects :
            p.drawEllipse(rr)
            p.drawText(rr,Qt.AlignCenter,rpoints[rects.index(rr)])

        rcenter=sum(centers)/len(centers)
        rhight=hight/2
        domin_name="baidu.com"
        rrect=QRect(rcenter-r,rhight-r,2*r,2*r)
        p.drawEllipse(rrect)
        p.drawText(rrect,Qt.AlignCenter,domin_name)

        for i in range(len(rpoints)) :
            p.drawLine(rcenter,rhight,centers[i],hight)

app=QApplication(sys.argv)
widget=mytest()
widget.show()
sys.exit(app.exec_())