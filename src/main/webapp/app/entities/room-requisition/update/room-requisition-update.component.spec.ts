import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RoomRequisitionFormService } from './room-requisition-form.service';
import { RoomRequisitionService } from '../service/room-requisition.service';
import { IRoomRequisition } from '../room-requisition.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IRoom } from 'app/entities/room/room.model';
import { RoomService } from 'app/entities/room/service/room.service';

import { RoomRequisitionUpdateComponent } from './room-requisition-update.component';

describe('RoomRequisition Management Update Component', () => {
  let comp: RoomRequisitionUpdateComponent;
  let fixture: ComponentFixture<RoomRequisitionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let roomRequisitionFormService: RoomRequisitionFormService;
  let roomRequisitionService: RoomRequisitionService;
  let userService: UserService;
  let employeeService: EmployeeService;
  let roomService: RoomService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RoomRequisitionUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(RoomRequisitionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RoomRequisitionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    roomRequisitionFormService = TestBed.inject(RoomRequisitionFormService);
    roomRequisitionService = TestBed.inject(RoomRequisitionService);
    userService = TestBed.inject(UserService);
    employeeService = TestBed.inject(EmployeeService);
    roomService = TestBed.inject(RoomService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const roomRequisition: IRoomRequisition = { id: 456 };
      const createdBy: IUser = { id: 87371 };
      roomRequisition.createdBy = createdBy;
      const updatedBy: IUser = { id: 19183 };
      roomRequisition.updatedBy = updatedBy;
      const sanctionedBy: IUser = { id: 32398 };
      roomRequisition.sanctionedBy = sanctionedBy;

      const userCollection: IUser[] = [{ id: 52284 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [createdBy, updatedBy, sanctionedBy];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ roomRequisition });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Employee query and add missing value', () => {
      const roomRequisition: IRoomRequisition = { id: 456 };
      const requester: IEmployee = { id: 15727 };
      roomRequisition.requester = requester;

      const employeeCollection: IEmployee[] = [{ id: 2508 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [requester];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ roomRequisition });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining)
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Room query and add missing value', () => {
      const roomRequisition: IRoomRequisition = { id: 456 };
      const room: IRoom = { id: 44694 };
      roomRequisition.room = room;

      const roomCollection: IRoom[] = [{ id: 29058 }];
      jest.spyOn(roomService, 'query').mockReturnValue(of(new HttpResponse({ body: roomCollection })));
      const additionalRooms = [room];
      const expectedCollection: IRoom[] = [...additionalRooms, ...roomCollection];
      jest.spyOn(roomService, 'addRoomToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ roomRequisition });
      comp.ngOnInit();

      expect(roomService.query).toHaveBeenCalled();
      expect(roomService.addRoomToCollectionIfMissing).toHaveBeenCalledWith(
        roomCollection,
        ...additionalRooms.map(expect.objectContaining)
      );
      expect(comp.roomsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const roomRequisition: IRoomRequisition = { id: 456 };
      const createdBy: IUser = { id: 582 };
      roomRequisition.createdBy = createdBy;
      const updatedBy: IUser = { id: 35048 };
      roomRequisition.updatedBy = updatedBy;
      const sanctionedBy: IUser = { id: 18144 };
      roomRequisition.sanctionedBy = sanctionedBy;
      const requester: IEmployee = { id: 47334 };
      roomRequisition.requester = requester;
      const room: IRoom = { id: 18565 };
      roomRequisition.room = room;

      activatedRoute.data = of({ roomRequisition });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(createdBy);
      expect(comp.usersSharedCollection).toContain(updatedBy);
      expect(comp.usersSharedCollection).toContain(sanctionedBy);
      expect(comp.employeesSharedCollection).toContain(requester);
      expect(comp.roomsSharedCollection).toContain(room);
      expect(comp.roomRequisition).toEqual(roomRequisition);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRoomRequisition>>();
      const roomRequisition = { id: 123 };
      jest.spyOn(roomRequisitionFormService, 'getRoomRequisition').mockReturnValue(roomRequisition);
      jest.spyOn(roomRequisitionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ roomRequisition });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: roomRequisition }));
      saveSubject.complete();

      // THEN
      expect(roomRequisitionFormService.getRoomRequisition).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(roomRequisitionService.update).toHaveBeenCalledWith(expect.objectContaining(roomRequisition));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRoomRequisition>>();
      const roomRequisition = { id: 123 };
      jest.spyOn(roomRequisitionFormService, 'getRoomRequisition').mockReturnValue({ id: null });
      jest.spyOn(roomRequisitionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ roomRequisition: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: roomRequisition }));
      saveSubject.complete();

      // THEN
      expect(roomRequisitionFormService.getRoomRequisition).toHaveBeenCalled();
      expect(roomRequisitionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRoomRequisition>>();
      const roomRequisition = { id: 123 };
      jest.spyOn(roomRequisitionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ roomRequisition });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(roomRequisitionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareEmployee', () => {
      it('Should forward to employeeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(employeeService, 'compareEmployee');
        comp.compareEmployee(entity, entity2);
        expect(employeeService.compareEmployee).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareRoom', () => {
      it('Should forward to roomService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(roomService, 'compareRoom');
        comp.compareRoom(entity, entity2);
        expect(roomService.compareRoom).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
