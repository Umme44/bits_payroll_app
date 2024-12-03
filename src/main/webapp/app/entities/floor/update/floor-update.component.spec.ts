import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FloorFormService } from './floor-form.service';
import { FloorService } from '../service/floor.service';
import { IFloor } from '../floor.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IBuilding } from 'app/entities/building/building.model';
import { BuildingService } from 'app/entities/building/service/building.service';

import { FloorUpdateComponent } from './floor-update.component';

describe('Floor Management Update Component', () => {
  let comp: FloorUpdateComponent;
  let fixture: ComponentFixture<FloorUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let floorFormService: FloorFormService;
  let floorService: FloorService;
  let userService: UserService;
  let buildingService: BuildingService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FloorUpdateComponent],
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
      .overrideTemplate(FloorUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FloorUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    floorFormService = TestBed.inject(FloorFormService);
    floorService = TestBed.inject(FloorService);
    userService = TestBed.inject(UserService);
    buildingService = TestBed.inject(BuildingService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const floor: IFloor = { id: 456 };
      const createdBy: IUser = { id: 88331 };
      floor.createdBy = createdBy;
      const updatedBy: IUser = { id: 7195 };
      floor.updatedBy = updatedBy;

      const userCollection: IUser[] = [{ id: 17868 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [createdBy, updatedBy];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ floor });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Building query and add missing value', () => {
      const floor: IFloor = { id: 456 };
      const building: IBuilding = { id: 25094 };
      floor.building = building;

      const buildingCollection: IBuilding[] = [{ id: 41339 }];
      jest.spyOn(buildingService, 'query').mockReturnValue(of(new HttpResponse({ body: buildingCollection })));
      const additionalBuildings = [building];
      const expectedCollection: IBuilding[] = [...additionalBuildings, ...buildingCollection];
      jest.spyOn(buildingService, 'addBuildingToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ floor });
      comp.ngOnInit();

      expect(buildingService.query).toHaveBeenCalled();
      expect(buildingService.addBuildingToCollectionIfMissing).toHaveBeenCalledWith(
        buildingCollection,
        ...additionalBuildings.map(expect.objectContaining)
      );
      expect(comp.buildingsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const floor: IFloor = { id: 456 };
      const createdBy: IUser = { id: 43204 };
      floor.createdBy = createdBy;
      const updatedBy: IUser = { id: 69918 };
      floor.updatedBy = updatedBy;
      const building: IBuilding = { id: 55295 };
      floor.building = building;

      activatedRoute.data = of({ floor });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(createdBy);
      expect(comp.usersSharedCollection).toContain(updatedBy);
      expect(comp.buildingsSharedCollection).toContain(building);
      expect(comp.floor).toEqual(floor);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFloor>>();
      const floor = { id: 123 };
      jest.spyOn(floorFormService, 'getFloor').mockReturnValue(floor);
      jest.spyOn(floorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ floor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: floor }));
      saveSubject.complete();

      // THEN
      expect(floorFormService.getFloor).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(floorService.update).toHaveBeenCalledWith(expect.objectContaining(floor));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFloor>>();
      const floor = { id: 123 };
      jest.spyOn(floorFormService, 'getFloor').mockReturnValue({ id: null });
      jest.spyOn(floorService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ floor: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: floor }));
      saveSubject.complete();

      // THEN
      expect(floorFormService.getFloor).toHaveBeenCalled();
      expect(floorService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFloor>>();
      const floor = { id: 123 };
      jest.spyOn(floorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ floor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(floorService.update).toHaveBeenCalled();
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
  });
});
