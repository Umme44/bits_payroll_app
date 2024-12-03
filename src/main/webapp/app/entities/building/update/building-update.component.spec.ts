import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BuildingFormService } from './building-form.service';
import { BuildingService } from '../service/building.service';
import { IBuilding } from '../building.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { BuildingUpdateComponent } from './building-update.component';

describe('Building Management Update Component', () => {
  let comp: BuildingUpdateComponent;
  let fixture: ComponentFixture<BuildingUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let buildingFormService: BuildingFormService;
  let buildingService: BuildingService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BuildingUpdateComponent],
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
      .overrideTemplate(BuildingUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BuildingUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    buildingFormService = TestBed.inject(BuildingFormService);
    buildingService = TestBed.inject(BuildingService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const building: IBuilding = { id: 456 };
      const createdBy: IUser = { id: 31564 };
      building.createdBy = createdBy;
      const updatedBy: IUser = { id: 59012 };
      building.updatedBy = updatedBy;

      const userCollection: IUser[] = [{ id: 87846 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [createdBy, updatedBy];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ building });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const building: IBuilding = { id: 456 };
      const createdBy: IUser = { id: 75822 };
      building.createdBy = createdBy;
      const updatedBy: IUser = { id: 28486 };
      building.updatedBy = updatedBy;

      activatedRoute.data = of({ building });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(createdBy);
      expect(comp.usersSharedCollection).toContain(updatedBy);
      expect(comp.building).toEqual(building);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBuilding>>();
      const building = { id: 123 };
      jest.spyOn(buildingFormService, 'getBuilding').mockReturnValue(building);
      jest.spyOn(buildingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ building });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: building }));
      saveSubject.complete();

      // THEN
      expect(buildingFormService.getBuilding).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(buildingService.update).toHaveBeenCalledWith(expect.objectContaining(building));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBuilding>>();
      const building = { id: 123 };
      jest.spyOn(buildingFormService, 'getBuilding').mockReturnValue({ id: null });
      jest.spyOn(buildingService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ building: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: building }));
      saveSubject.complete();

      // THEN
      expect(buildingFormService.getBuilding).toHaveBeenCalled();
      expect(buildingService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBuilding>>();
      const building = { id: 123 };
      jest.spyOn(buildingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ building });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(buildingService.update).toHaveBeenCalled();
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
