import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PfAccountDetailComponent } from './pf-account-detail.component';

describe('PfAccount Management Detail Component', () => {
  let comp: PfAccountDetailComponent;
  let fixture: ComponentFixture<PfAccountDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PfAccountDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ pfAccount: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PfAccountDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PfAccountDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load pfAccount on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.pfAccount).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
