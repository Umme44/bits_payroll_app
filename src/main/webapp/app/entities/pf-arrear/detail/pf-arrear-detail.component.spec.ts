import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PfArrearDetailComponent } from './pf-arrear-detail.component';

describe('PfArrear Management Detail Component', () => {
  let comp: PfArrearDetailComponent;
  let fixture: ComponentFixture<PfArrearDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PfArrearDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ pfArrear: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PfArrearDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PfArrearDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load pfArrear on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.pfArrear).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
