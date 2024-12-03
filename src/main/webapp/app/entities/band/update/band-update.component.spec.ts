import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BandFormService } from './band-form.service';
import { BandService } from '../service/band.service';
import { IBand } from '../band.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { BandUpdateComponent } from './band-update.component';

describe('Band Management Update Component', () => {
  let comp: BandUpdateComponent;
  let fixture: ComponentFixture<BandUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let bandFormService: BandFormService;
  let bandService: BandService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BandUpdateComponent],
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
      .overrideTemplate(BandUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BandUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    bandFormService = TestBed.inject(BandFormService);
    bandService = TestBed.inject(BandService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const band: IBand = { id: 456 };
      const createdBy: IUser = { id: 68371 };
      band.createdBy = createdBy;
      const updatedBy: IUser = { id: 6635 };
      band.updatedBy = updatedBy;

      const userCollection: IUser[] = [{ id: 55929 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [createdBy, updatedBy];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ band });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const band: IBand = { id: 456 };
      const createdBy: IUser = { id: 90125 };
      band.createdBy = createdBy;
      const updatedBy: IUser = { id: 80509 };
      band.updatedBy = updatedBy;

      activatedRoute.data = of({ band });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(createdBy);
      expect(comp.usersSharedCollection).toContain(updatedBy);
      expect(comp.band).toEqual(band);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBand>>();
      const band = { id: 123 };
      jest.spyOn(bandFormService, 'getBand').mockReturnValue(band);
      jest.spyOn(bandService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ band });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: band }));
      saveSubject.complete();

      // THEN
      expect(bandFormService.getBand).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(bandService.update).toHaveBeenCalledWith(expect.objectContaining(band));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBand>>();
      const band = { id: 123 };
      jest.spyOn(bandFormService, 'getBand').mockReturnValue({ id: null });
      jest.spyOn(bandService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ band: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: band }));
      saveSubject.complete();

      // THEN
      expect(bandFormService.getBand).toHaveBeenCalled();
      expect(bandService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBand>>();
      const band = { id: 123 };
      jest.spyOn(bandService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ band });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(bandService.update).toHaveBeenCalled();
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
