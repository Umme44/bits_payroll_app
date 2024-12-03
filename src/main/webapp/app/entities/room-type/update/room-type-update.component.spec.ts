import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RoomTypeFormService } from './room-type-form.service';
import { RoomTypeService } from '../service/room-type.service';
import { IRoomType } from '../room-type.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { RoomTypeUpdateComponent } from './room-type-update.component';

describe('RoomType Management Update Component', () => {
  let comp: RoomTypeUpdateComponent;
  let fixture: ComponentFixture<RoomTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let roomTypeFormService: RoomTypeFormService;
  let roomTypeService: RoomTypeService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RoomTypeUpdateComponent],
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
      .overrideTemplate(RoomTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RoomTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    roomTypeFormService = TestBed.inject(RoomTypeFormService);
    roomTypeService = TestBed.inject(RoomTypeService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const roomType: IRoomType = { id: 456 };
      const createdBy: IUser = { id: 30681 };
      roomType.createdBy = createdBy;
      const updatedBy: IUser = { id: 70669 };
      roomType.updatedBy = updatedBy;

      const userCollection: IUser[] = [{ id: 38443 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [createdBy, updatedBy];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ roomType });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const roomType: IRoomType = { id: 456 };
      const createdBy: IUser = { id: 40045 };
      roomType.createdBy = createdBy;
      const updatedBy: IUser = { id: 78146 };
      roomType.updatedBy = updatedBy;

      activatedRoute.data = of({ roomType });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(createdBy);
      expect(comp.usersSharedCollection).toContain(updatedBy);
      expect(comp.roomType).toEqual(roomType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRoomType>>();
      const roomType = { id: 123 };
      jest.spyOn(roomTypeFormService, 'getRoomType').mockReturnValue(roomType);
      jest.spyOn(roomTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ roomType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: roomType }));
      saveSubject.complete();

      // THEN
      expect(roomTypeFormService.getRoomType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(roomTypeService.update).toHaveBeenCalledWith(expect.objectContaining(roomType));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRoomType>>();
      const roomType = { id: 123 };
      jest.spyOn(roomTypeFormService, 'getRoomType').mockReturnValue({ id: null });
      jest.spyOn(roomTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ roomType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: roomType }));
      saveSubject.complete();

      // THEN
      expect(roomTypeFormService.getRoomType).toHaveBeenCalled();
      expect(roomTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRoomType>>();
      const roomType = { id: 123 };
      jest.spyOn(roomTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ roomType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(roomTypeService.update).toHaveBeenCalled();
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
  });
});
