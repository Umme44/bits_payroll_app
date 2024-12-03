import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RoomFormService } from './room-form.service';
import { RoomService } from '../service/room.service';
import { IRoom } from '../room.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IBuilding } from 'app/entities/building/building.model';
import { BuildingService } from 'app/entities/building/service/building.service';
import { IFloor } from 'app/entities/floor/floor.model';
import { FloorService } from 'app/entities/floor/service/floor.service';
import { IRoomType } from 'app/entities/room-type/room-type.model';
import { RoomTypeService } from 'app/entities/room-type/service/room-type.service';

import { RoomUpdateComponent } from './room-update.component';

describe('Room Management Update Component', () => {
  let comp: RoomUpdateComponent;
  let fixture: ComponentFixture<RoomUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let roomFormService: RoomFormService;
  let roomService: RoomService;
  let userService: UserService;
  let buildingService: BuildingService;
  let floorService: FloorService;
  let roomTypeService: RoomTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RoomUpdateComponent],
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
      .overrideTemplate(RoomUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RoomUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    roomFormService = TestBed.inject(RoomFormService);
    roomService = TestBed.inject(RoomService);
    userService = TestBed.inject(UserService);
    buildingService = TestBed.inject(BuildingService);
    floorService = TestBed.inject(FloorService);
    roomTypeService = TestBed.inject(RoomTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const room: IRoom = { id: 456 };
      const createdBy: IUser = { id: 69795 };
      room.createdBy = createdBy;
      const updatedBy: IUser = { id: 81486 };
      room.updatedBy = updatedBy;

      const userCollection: IUser[] = [{ id: 31602 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [createdBy, updatedBy];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ room });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Building query and add missing value', () => {
      const room: IRoom = { id: 456 };
      const building: IBuilding = { id: 68417 };
      room.building = building;

      const buildingCollection: IBuilding[] = [{ id: 10589 }];
      jest.spyOn(buildingService, 'query').mockReturnValue(of(new HttpResponse({ body: buildingCollection })));
      const additionalBuildings = [building];
      const expectedCollection: IBuilding[] = [...additionalBuildings, ...buildingCollection];
      jest.spyOn(buildingService, 'addBuildingToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ room });
      comp.ngOnInit();

      expect(buildingService.query).toHaveBeenCalled();
      expect(buildingService.addBuildingToCollectionIfMissing).toHaveBeenCalledWith(
        buildingCollection,
        ...additionalBuildings.map(expect.objectContaining)
      );
      expect(comp.buildingsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Floor query and add missing value', () => {
      const room: IRoom = { id: 456 };
      const floor: IFloor = { id: 71631 };
      room.floor = floor;

      const floorCollection: IFloor[] = [{ id: 94723 }];
      jest.spyOn(floorService, 'query').mockReturnValue(of(new HttpResponse({ body: floorCollection })));
      const additionalFloors = [floor];
      const expectedCollection: IFloor[] = [...additionalFloors, ...floorCollection];
      jest.spyOn(floorService, 'addFloorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ room });
      comp.ngOnInit();

      expect(floorService.query).toHaveBeenCalled();
      expect(floorService.addFloorToCollectionIfMissing).toHaveBeenCalledWith(
        floorCollection,
        ...additionalFloors.map(expect.objectContaining)
      );
      expect(comp.floorsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call RoomType query and add missing value', () => {
      const room: IRoom = { id: 456 };
      const roomType: IRoomType = { id: 95775 };
      room.roomType = roomType;

      const roomTypeCollection: IRoomType[] = [{ id: 77972 }];
      jest.spyOn(roomTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: roomTypeCollection })));
      const additionalRoomTypes = [roomType];
      const expectedCollection: IRoomType[] = [...additionalRoomTypes, ...roomTypeCollection];
      jest.spyOn(roomTypeService, 'addRoomTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ room });
      comp.ngOnInit();

      expect(roomTypeService.query).toHaveBeenCalled();
      expect(roomTypeService.addRoomTypeToCollectionIfMissing).toHaveBeenCalledWith(
        roomTypeCollection,
        ...additionalRoomTypes.map(expect.objectContaining)
      );
      expect(comp.roomTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const room: IRoom = { id: 456 };
      const createdBy: IUser = { id: 67439 };
      room.createdBy = createdBy;
      const updatedBy: IUser = { id: 64061 };
      room.updatedBy = updatedBy;
      const building: IBuilding = { id: 73329 };
      room.building = building;
      const floor: IFloor = { id: 1994 };
      room.floor = floor;
      const roomType: IRoomType = { id: 97514 };
      room.roomType = roomType;

      activatedRoute.data = of({ room });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(createdBy);
      expect(comp.usersSharedCollection).toContain(updatedBy);
      expect(comp.buildingsSharedCollection).toContain(building);
      expect(comp.floorsSharedCollection).toContain(floor);
      expect(comp.roomTypesSharedCollection).toContain(roomType);
      expect(comp.room).toEqual(room);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRoom>>();
      const room = { id: 123 };
      jest.spyOn(roomFormService, 'getRoom').mockReturnValue(room);
      jest.spyOn(roomService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ room });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: room }));
      saveSubject.complete();

      // THEN
      expect(roomFormService.getRoom).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(roomService.update).toHaveBeenCalledWith(expect.objectContaining(room));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRoom>>();
      const room = { id: 123 };
      jest.spyOn(roomFormService, 'getRoom').mockReturnValue({ id: null });
      jest.spyOn(roomService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ room: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: room }));
      saveSubject.complete();

      // THEN
      expect(roomFormService.getRoom).toHaveBeenCalled();
      expect(roomService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRoom>>();
      const room = { id: 123 };
      jest.spyOn(roomService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ room });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(roomService.update).toHaveBeenCalled();
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

    describe('compareBuilding', () => {
      it('Should forward to buildingService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(buildingService, 'compareBuilding');
        comp.compareBuilding(entity, entity2);
        expect(buildingService.compareBuilding).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareFloor', () => {
      it('Should forward to floorService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(floorService, 'compareFloor');
        comp.compareFloor(entity, entity2);
        expect(floorService.compareFloor).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareRoomType', () => {
      it('Should forward to roomTypeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(roomTypeService, 'compareRoomType');
        comp.compareRoomType(entity, entity2);
        expect(roomTypeService.compareRoomType).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
