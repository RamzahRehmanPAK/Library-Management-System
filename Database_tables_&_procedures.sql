create schema librarySystem
go
CREATE TABLE librarySystem.AdminInfo(
   usename varchar(30) not null,
   userPassword varchar(30) not null,
   PRIMARY KEY(usename)
);
go

CREATE TABLE librarySystem.Book(	
   ID char (13) ,
   Titile varchar(30) not null,
   quantity int  not null,
   Authors varchar (200) not null,
   available int not null
   PRIMARY KEY(ID)
);
go

CREATE TABLE librarySystem.Issue(	
   issueID int IDENTITY(1,1),
   issueDate Date not null,
   memberID int foreign key references  librarySystem.Member(ID) on delete cascade ,
  bookID char(13) foreign key references librarySystem.Book(ID),
   returnDate Date  null,
   PRIMARY KEY(issueID)
);
go

CREATE TABLE librarySystem.Member(	
   ID int IDENTITY(1,1),
   memberName varchar(30) not null,
   fine int not null,
   phoneNo char(11) not null,
   address varchar (100) not null,
   issuedBooks int not null
   PRIMARY KEY(ID)
);
go

--queries 

 CREATE PROCEDURE librarySystem.checkLogin

    @username VARCHAR(30),
    @password varchar(30),
	@isvalid int OUT
AS
BEGIN

IF EXISTS(SELECT * FROM librarySystem.AdminInfo A WHERE A.usename = @username AND A.userPassword = @password)
    begin
    set @isvalid=1
	end
ELSE
	begin
	set @isvalid=0;
	 end
END
go

Create procedure librarySystem.addMember
@name varchar(30),
@phone char (11),
@address varchar(100),
@out int out
as
begin
	set @out=-1;
	if not exists (select * from librarySystem.Member M where lower(M.memberName)=lower(@name) and M.phoneNo=@phone)
	begin
		insert into librarySystem.Member values( @name,0,@phone,@address,0);
		select @out=M.ID from librarySystem.Member M where M.memberName=@name and M.phoneNo=@phone
	end
end
go

Create procedure librarySystem.addBook
@title varchar(30),
@isbn char (13),
@authors varchar(200),
@quantity int 
as
begin
	if not exists (select * from librarySystem.Book B where  B.ID=@isbn)
	begin
		insert into librarySystem.Book values( @isbn,@title,@quantity,@authors,@quantity);
	end
end
go


create procedure librarySystem.removeBook
@ISBN char(13),
@remove int,-- -1
@out int out -- 0 
as
begin
	declare @quantity int
	declare @available int
		
	select  @quantity=B.quantity, @available=B.available from librarySystem.Book B where B.ID=@ISBN

	set @out=0

	if  @available>0
		begin
		
		if @remove=-1
			begin
			set @remove=@available
			end

		if @available>=@remove
			begin
			set @quantity=@quantity-@remove
			set @available=@available-@remove
			
			update librarySystem.Book 
			set quantity=@quantity, available=@available
			where ID=@ISBN

			set @out=1

			end

		end
end
go

create procedure librarySystem.IssueBook
@ID int,
@ISBN varchar(500),
@msg varchar(50) out
as
begin
	set @msg='correct'
	if exists (select * from librarySystem.Member M where M.ID=@ID )
	begin

		declare @fine int
		declare @issued int
		select @fine=fine,@issued=issuedBooks
		from librarySystem.Member where ID=@ID

		if @fine>=300 --can't issue if fine >=300
		begin
			set @msg='Fine greater than 300, member  can not issue a book!'
		end

		else if @issued=4 --can't issue more than 4
		begin
			set @msg='Member can not issue more than 4 books'
		end

		else
		begin
			if exists (select * from librarySystem.Book B where B.ID=@ISBN )
			begin
				if exists( select * from Issue I 
							where I.memberID=@ID and I.bookID=@ISBN and returnDate IS NULL)
				begin
				set @msg='Already issued!'
				end

				else
				begin
				
					declare @available int
					set @available=0
							select @available=B.available from librarySystem.Book B where B.ID=@ISBN

					if @available!=0
						begin
							update librarySystem.Book set available=available-1 where ID=@ISBN

							insert into librarySystem.Issue values(GETDATE(),@ID,@ISBN,null)

							update librarySystem.Member set issuedBooks=issuedBooks+1
							where ID=@ID

						end
					else
						begin
						set @msg='Book is not currently available'
						end
				end
			end
				
			else
			begin
				set @msg='Book ISBN invalid'
			end 
		end

	end
	
	else
	begin
		set @msg='Member ID invalid!'
	end
end
go

create procedure librarySystem.ReturnBook
@ID int,
@ISBN varchar(500),
@msg varchar(50) out
as
begin
	set @msg='correct'
	if exists (select * from librarySystem.Member M where M.ID=@ID )
	begin
		if exists (select * from librarySystem.Book B where B.ID=@ISBN)
		begin
			if exists (select * from librarySystem.Issue I
						 where I.memberID=@ID and I.bookID=@ISBN and I.returnDate IS NULL )
				begin
					declare @fine int,@start date
					

					select  @start=I.issueDate
					from librarySystem.Issue I
					where I.memberID=@ID and I.bookID=@ISBN and I.returnDate IS NULL

					declare @latedays int 

					set @latedays= datediff(day,@start,getDate())

					set @fine=0
					if @latedays>10
					begin
						set @fine=(@latedays-10) * 5 --5 ruppee fine per day
					end

					update librarySystem.Issue set returnDate=GETDATE()
					where memberID=@ID and bookID=@ISBN and returnDate IS NULL

					update librarySystem.Member set fine=fine+@fine, issuedBooks=issuedBooks-1
					 where ID=@ID

					 update librarySystem.Book set available=available+1
					 where ID=@ISBN

				end
			else
				begin
				set @msg='This book is not currently issued to specified member!'
				end
		end
		else
		begin
			set @msg='Book ISBN invalid'
		end 
	end
	else
	begin
		set @msg='Member ID invalid!'
	end
end
go

create procedure librarySystem.receiveFine
@ID int,
@amount int,
@msg varchar(50) out
as
begin
	set @msg='correct'
	if exists (select * from librarySystem.Member M where M.ID=@ID )
	begin
		declare @payable int

		select @payable=fine 
		from librarySystem.Member
		where ID=@ID

		if @amount<=@payable
		begin
			update librarySystem.Member
			set fine=@payable-@amount
			where ID=@ID
		end

		else
		begin
			set @msg ='Too much amount to pay fine!'
		end
	end
	else
	begin
		set @msg='Member ID invalid!'
	end
end
go