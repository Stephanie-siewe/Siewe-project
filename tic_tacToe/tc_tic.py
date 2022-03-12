import tkinter as tk
from tkinter import messagebox


class FrameInit(tk.Frame):
    def __init__(self, master, bg, width, height):
        tk.Frame.__init__(self, master, bg=bg)

        # create canvas
        self.canvas = tk.Canvas(self, width=width, height=height, bg=bg, bd=0, highlightthickness=0)
        self.canvas.create_image(width / 2, height / 2, image=image_i)

        # create play button
        self.button = tk.Button(self.master, width=10, text="Play", font=("Germanica", 25), bg="black", fg="#660099",
                                bd=0, command=lambda: self.click_start(self))
        self.button.pack(pady=35, side=tk.BOTTOM)

        self.canvas.pack()
        self.pack(expand=tk.YES)
        print("init frame exist:", self.winfo_exists())

    def click_start(self, widget):
        self.destroy()
        self.button.destroy()
        if not self.winfo_exists():
            print("init frame exist:", self.winfo_exists())

            # create the grill frame
            g_frame = GameFrame(self.master, bg="#6600CC", bd=5, highlightcolor="#6600CC")


class GameFrame(tk.Frame):
    def __init__(self, master, bg, bd, highlightcolor):
        tk.Frame.__init__(self, master, bg=bg, bd=bd, highlightcolor=highlightcolor)
        self.clicked, self.count, self.winner = True, 0, False

        self.buttons = [tk.Button(self, text=" ", font=("Arial Black", 25), bg="Black", fg="#6600CC", bd=5,
                                  highlightthickness=4, highlightcolor="#6600CC", highlightbackground="#6600CC",
                                  borderwidth=4,
                                  height=3, width=6) for i in range(9)]

        self.buttons[0]["command"] = lambda: self.click_case(self.buttons[0])
        self.buttons[1]["command"] = lambda: self.click_case(self.buttons[1])
        self.buttons[2]["command"] = lambda: self.click_case(self.buttons[2])
        self.buttons[3]["command"] = lambda: self.click_case(self.buttons[3])
        self.buttons[4]["command"] = lambda: self.click_case(self.buttons[4])
        self.buttons[5]["command"] = lambda: self.click_case(self.buttons[5])
        self.buttons[6]["command"] = lambda: self.click_case(self.buttons[6])
        self.buttons[7]["command"] = lambda: self.click_case(self.buttons[7])
        self.buttons[8]["command"] = lambda: self.click_case(self.buttons[8])

        for i in range(9):
            self.buttons[i].grid(row=i // 3, column=(i % 3))

        self.retry_but = tk.Button(self, width=20, text="Retry ?", font=("Arial Black", 25), bg="pink",
                                   fg="#660099", bd=0, command=lambda: self.retry_game())
        self.quit_but = tk.Button(self, width=20, text="Exit", font=("Arial Black", 25), bg="black",
                                  fg="#660099", bd=0, command=lambda: self.master.quit())

        self.pack(expand=tk.YES)

    def disable_all_buttons(self):
        for i in range(len(self.buttons)):
            self.buttons[i].config(state=tk.DISABLED)

    def click_case(self, widget):
        if widget["text"] == " " and self.clicked:
            self.clicked = False
            self.count += 1
            widget["text"] = "X"
            self.check_winner()
        elif widget["text"] == " " and not self.clicked:
            self.clicked = True
            self.count += 1
            widget["text"] = "0"
            self.check_winner()
        else:
            messagebox.showerror("Error", "This box isn't empty")

    def check_winner(self):
        i, eq = 0, True
        while not self.winner and i < len(self.buttons):
            if i % 3 == 0 and self.buttons[i]["text"] in ["X", "0"] and \
                    self.buttons[i + 1]["text"] == self.buttons[i]["text"] and \
                    self.buttons[i + 2]["text"] == self.buttons[i]["text"]:
                self.buttons[i]["bg"] = self.buttons[i + 1]["bg"] = self.buttons[i + 2]["bg"] = "#006699"
                self.winner = True
                messagebox.showinfo("Winner", "Congratulation {} you're winner".format(self.buttons[i]["text"]))
                self.disable_all_buttons()
            elif i < 3 and self.buttons[i]["text"] in ["X", "0"] and \
                    self.buttons[i + 3]["text"] == self.buttons[i]["text"] and \
                    self.buttons[i + 2 * 3]["text"] == self.buttons[i]["text"]:
                self.buttons[i]["bg"] = self.buttons[i + 3]["bg"] = self.buttons[i + 2 * 3]["bg"] = "#006699"
                self.winner = True
                messagebox.showinfo("Winner", "Congratulation {} you're winner".format(self.buttons[i]["text"]))
                self.disable_all_buttons()
            elif i == 0 and self.buttons[i]["text"] in ["X", "0"] and \
                    self.buttons[i + 4]["text"] == self.buttons[i]["text"] and \
                    self.buttons[i + 8]["text"] == self.buttons[i]["text"]:
                self.buttons[i]["bg"] = self.buttons[i + 4]["bg"] = self.buttons[i + 8]["bg"] = "#006699"
                self.winner = True
                messagebox.showinfo("Winner", "Congratulation {} you're winner".format(self.buttons[i]["text"]))
                self.disable_all_buttons()
            elif i == 2 and self.buttons[i]["text"] in ["X", "0"] and \
                    self.buttons[i + 2]["text"] == self.buttons[i]["text"] and \
                    self.buttons[i + 4]["text"] == self.buttons[i]["text"]:
                self.buttons[i]["bg"] = self.buttons[i + 2]["bg"] = self.buttons[i + 4]["bg"] = "#006699"
                self.winner = True
                messagebox.showinfo("Winner", "Congratulation {} you're winner".format(self.buttons[i]["text"]))
                self.disable_all_buttons()

            if eq and self.buttons[i]["text"] == " ":
                eq = False
            elif eq and i == len(self.buttons)-1:
                messagebox.showinfo("Equality", "Nobody won")
            i += 1

        if self.winner or eq:
            for i in range(len(self.buttons)):
                self.buttons[i].destroy()
            self["bg"], self.master["bg"] = "#c8c9d4", "#c8c9d4"
            self.retry_but.pack(pady=50, side=tk.TOP)
            self.quit_but.pack(pady=50, side=tk.BOTTOM)

    def retry_game(self):
        self.retry_but.destroy()
        self.quit_but.destroy()
        g_frame = GameFrame(self.master, bg="#6600CC", bd=5, highlightcolor="#6600CC")
        self.destroy()


if __name__ == '__main__':
    window = tk.Tk()
    window.title("Tic-Tac-Toe")
    window.iconbitmap("logo tic.ico")
    window.config(background="black")
    window.minsize(480, 360)

    image_i = tk.PhotoImage(file="tic.png").zoom(35).subsample(32)

    frame_i = FrameInit(window, bg="black", width=300, height=300)

    window.mainloop()
